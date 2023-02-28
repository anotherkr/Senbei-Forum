package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernAddRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernQueryRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernRemoveRequest;
import com.yhz.senbeiforummain.model.vo.ModuleConcernTopicVo;
import com.yhz.senbeiforummain.service.IModuleConcernService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2023/2/20 - 13:51
 */
@RestController
@Slf4j
@Api(tags = "关注功能")
@RequestMapping("/concern")
public class ConcernController {
    @Resource
    IModuleConcernService IModuleConcernService;

    @PostMapping("/module")
    @ApiOperation("关注模块")
    public BaseResponse concernModule(@RequestBody ModuleConcernAddRequest moduleConcernAddRequest, @UserId Long userId) {
        boolean save = IModuleConcernService.addModuleConcern(moduleConcernAddRequest,userId);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return ResultUtils.success();
    }
    @PostMapping("/cancel")
    @ApiOperation("取消关注模块")
    public BaseResponse cancelConcernModule(@RequestBody ModuleConcernRemoveRequest moduleConcernRemoveRequest) {
        IModuleConcernService.cancelModuleConcern(moduleConcernRemoveRequest);

        return ResultUtils.success();
    }
    @PostMapping("/module/page")
    @ApiOperation(value = "获取关注模块的帖子并分页",tags = "默认按时间降序排序")
    public BaseResponse getModuleConcernTopicByPage(@RequestBody ModuleConcernQueryRequest moduleConcernQueryRequest) {
        IPage<ModuleConcernTopicVo> iPage = IModuleConcernService.getModuleConcernTopicByPage(moduleConcernQueryRequest);
        return ResultUtils.success(iPage);
    }

}
