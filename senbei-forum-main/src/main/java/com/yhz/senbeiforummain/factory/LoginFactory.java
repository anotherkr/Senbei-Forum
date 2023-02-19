package com.yhz.senbeiforummain.factory;

import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.handler.OauthLoginHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录处理器工厂
 * @author yanhuanzhan
 * @date 2022/12/15 - 22:07
 */
@Component
public class LoginFactory implements InitializingBean {
    @Resource
    private List<OauthLoginHandler> loginHandlerList;
    private Map<LoginChannelEnum, OauthLoginHandler> handlerMap = new HashMap<>();

    public OauthLoginHandler getHandlerByCode(int code) {
        LoginChannelEnum loginChannelEnum = LoginChannelEnum.getByCode(code);
        return handlerMap.get(loginChannelEnum);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        loginHandlerList.forEach(loginHandler -> {
            handlerMap.put(loginHandler.getChannel(), loginHandler);
        });
    }

    public OauthLoginHandler getHandler(LoginChannelEnum loginChannelEnum) {
        return handlerMap.get(loginChannelEnum);
    }
}
