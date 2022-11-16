package com.wxs.springbootweight.util;

import com.wxs.springbootweight.entity.ResultVO;

/**
 * @author xiaoshuai.wei
 * 返回结果封装工具类
 */
public class ResultUtil {
    /**
     * 请求成功返回
     *
     * @param data
     * @return
     */
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<T>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }


    public static <T> ResultVO<T> error() {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMsg(ResultEnum.ERROR.getMessage());
        return result;
    }

}
