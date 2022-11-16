package com.wxs.springbootweight.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoshuai.wei
 * 统一结果集类
 */
@Data
public class ResultVO<T> implements Serializable {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

}
