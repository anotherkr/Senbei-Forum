package com.yhz.commonutil.util;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 图片工具类
 * @author yanhuanzhan
 * @date 2022/11/27 - 12:32
 */
public class ImgUrlUtil {
    public static String imgUrlArrayToJson(String[] imgUrlArray) {
        if (imgUrlArray.length > 0) {
            Gson gson = new Gson();
            String imgUrlToJson = gson.toJson(imgUrlArray);
            return imgUrlToJson;
        }
        return null;
    }

    public static String[] imgUrlJsonToArray(String imgUrlJson) {
        if (StrUtil.isNotBlank(imgUrlJson)) {
            Gson gson = new Gson();
            String[] imgUrlArray = gson.fromJson(imgUrlJson, String[].class);
            return imgUrlArray;
        }
        return null;
    }
}
