package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisCaptchaKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisUserKey;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.dto.register.EmailRegisterRequest;
import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.model.enums.RoleEnum;
import com.yhz.senbeiforummain.factory.LoginFactory;
import com.yhz.senbeiforummain.handler.OauthLoginHandler;
import com.yhz.senbeiforummain.model.vo.CaptchaImageVo;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.model.dto.login.DoLoginRequest;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.ILoginService;
import com.yhz.senbeiforummain.service.IRoleService;
import com.yhz.senbeiforummain.service.IUserService;
import com.yhz.senbeiforummain.util.Base64Utils;
import com.yhz.senbeiforummain.util.JwtUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * @author 吉良吉影
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Value("${spring.mail.username}")
    private String from;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private IUserService userService;
    @Resource
    private IRoleService roleService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private LoginFactory loginFactory;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RedisCache redisCache;
    @Resource
    private JavaMailSender mailSender;
    /**
     * 通过配置文件自定义配置
     */
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Override
    public String doLogin(DoLoginRequest doLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        //校验验证码
        String captchaCode = doLoginRequest.getCaptchaCode();
        String uuid = doLoginRequest.getUuid();
        String realCaptchaCode = redisCache.getCacheObject(RedisCaptchaKey.getCaptchaCode, uuid);
        if (realCaptchaCode == null || !realCaptchaCode.equals(captchaCode)) {
            throw new BusinessException(ErrorCode.CODE_ERROR);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(doLoginRequest.getUsername(), doLoginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            //用户名密码错误
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        AuthUser authUser = (AuthUser) authenticate.getPrincipal();
        String username = authUser.getUser().getUsername();
        String token = JwtUtil.createJWT(username);

        //把token和用户信息存到redis中
        redisCache.setCacheObject(RedisUserKey.getUserToken, username, token);
        redisCache.setCacheObject(RedisUserKey.getUserInfo, username, authUser);

        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return token;
    }

    @Override
    public void doLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            //如果是匿名用户
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String username = authUser.getUser().getUsername();
        //删除redis中存的信息
        redisCache.deleteObject(RedisUserKey.getUserToken, username);
        redisCache.deleteObject(RedisUserKey.getUserInfo, username);
        //清除上下文
        SecurityContextHolder.clearContext();

    }

    @Override
    public void mailSend(String email) {
        //先从redis里面获取验证码，如果获取不到则生成验证码并进行发送
        String checkCode = redisCache.getCacheObject(RedisUserKey.getUserEmailCode, email);
        if (StrUtil.isEmpty(checkCode)) {
            checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
            String message = "您的注册验证码为：".concat(checkCode).concat("(有效时间5分钟),请在有效时间内完成注册。");
            sendSimpleMail(email, "仙贝论坛注册验证码", message);
            //存到redis中并设置有效时间5分钟
            redisCache.setCacheObject(RedisUserKey.getUserEmailCode, email, checkCode);
        }

    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void emailRegister(EmailRegisterRequest registerDto) {
        String email = registerDto.getEmail();
        String password = registerDto.getPassword();
        String checkPassword = registerDto.getCheckPassword();
        String nickname = registerDto.getNickname();
        String code = registerDto.getCode();
        if (StrUtil.isEmpty(email) || StrUtil.isEmpty(password) ||
                StrUtil.isEmpty(checkPassword) || StrUtil.isEmpty(nickname) || StrUtil.isEmpty(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断验证码
        String checkCode = redisCache.getCacheObject(RedisUserKey.getUserEmailCode, email);
        if (!code.equals(checkCode)) {
            throw new BusinessException("验证码错误", 400, "");
        }
        //判断邮箱是否重复
        Integer count = userService.count(new QueryWrapper<User>().eq("email", email));
        if (count > 0) {
            throw new BusinessException("邮箱已经被注册", 400, "");
        }
        //数据添加到数据库中
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        registerDto.setPassword("{bcrypt}" + encodePassword);
        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        //使用邮箱作为用户名
        user.setUsername(email);
        transactionTemplate.execute(transactionStatus -> {
            boolean save = userService.save(user);
            if (!save) {
                throw new BusinessException(ErrorCode.SAVE_ERROR);
            }
            //设置角色
            if (!roleService.attachRoleByUserId(RoleEnum.ROLE_USER.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.SAVE_ERROR);
            }
            return null;
        });

    }

    @Override
    public String getOauthUrl(LoginChannelEnum loginChannelEnum) {
        OauthLoginHandler oauthLoginHandler = loginFactory.getHandler(loginChannelEnum);
        return oauthLoginHandler.getUrl();
    }

    @Override
    public void oauthLogin(LoginChannelEnum loginChannelEnum, String code, HttpServletResponse response) throws IOException {
        OauthLoginHandler handler = loginFactory.getHandler(loginChannelEnum);
        handler.dealLogin(code, response);
    }

    @Override
    public CaptchaImageVo createCaptchImage() {
        //        生成UUID
        String uuid = IdUtil.simpleUUID();
//        获取验证码
        String capStr = null, code = null;
        BufferedImage image = null;
        // 生成验证
        String captchaText = captchaProducerMath.createText();
        capStr = captchaText.substring(0, captchaText.lastIndexOf("@"));
        code = captchaText.substring(captchaText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
//        将验证码存放到redis中
        redisCache.setCacheObject(RedisCaptchaKey.getCaptchaCode, uuid, code);

//        转换为流对象写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            logger.error("image io write error:{}", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        CaptchaImageVo captchaImageVo = new CaptchaImageVo();
        captchaImageVo.setUuid(uuid);
        captchaImageVo.setImgBase64(Base64Utils.encode(os.toByteArray()));
        return captchaImageVo;
    }



    private void sendSimpleMail(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("mail send error,msg:{}", e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }
}
