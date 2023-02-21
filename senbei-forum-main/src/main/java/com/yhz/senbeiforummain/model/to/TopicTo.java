package com.yhz.senbeiforummain.model.to;

import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2023/2/20 - 16:09
 */
@Data
public class TopicTo {

    private Long id;


    private ModuleVo moduleVo;
    /**
     * 模块id
     */

    private Long moduleId;

    /**
     * 模块背景图片
     */

    private String moduleBackgroundImgUrl;


    public String moduleName;
    /**
     * 主贴标题
     */

    private String title;


    private String content;


    /**
     * 图片地址(0-3张图片)
     */
    private String imgUrls;


    private UserInfoVo userInfoVo;



    private Integer replyNum;


    private Integer supportNum;


    private Integer unsupportNum;


    private Integer heat;


    private Date createTime;
}
