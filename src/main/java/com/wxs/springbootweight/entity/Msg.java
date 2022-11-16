package com.wxs.springbootweight.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName msg
 */
@TableName(value = "msg")
@Data
public class Msg implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 称重类型("1"PP "2"料号)
     */
    @Alias("称重类型")
    private String type;
    /**
     * PP型号/料号
     */
    @Alias("PP型号/料号")
    @TableField(value = "ppType")
    private String ppType;
    /**
     * 尺寸经纬度
     */
    @Alias("经*纬")
    private String size;
    /**
     * 含胶量
     */
    @Alias("含胶量")
    private String rc;
    /**
     * 最小值
     */
    @Alias("最小值")
    private Double min;
    /**
     * 最大值
     */
    @Alias("最大值")
    private Double max;
}