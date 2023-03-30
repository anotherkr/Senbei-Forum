package com.yhz.senbeiforummain.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yhz.commonutil.common.PageRequest;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2023/3/26 - 17:35
 */
@Data
public class UserReplyRequest extends PageRequest {
    @ApiModelProperty("用户id")
    private Long userId;

}
