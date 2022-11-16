package com.wxs.springbootweight.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaoshuai.wei
 * @TableName weight_home
 * 称重历史记录类
 */
@TableName(value = "weight_home")
@Data
public class WeightHome implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 工单
     */
    @Alias("工单")
    private String gd;
    /**
     * 称重类型("1"PP "2"料号)
     */
    @Alias("称重类型")
    private String type;
    /**
     * 料号
     */
    @Alias("料号")
    private String pin;
    /**
     * PP型号
     */
    @Alias("PP型号")
    @TableField(value = "ppType")
    private String ppType;
    /**
     * 含胶量
     */
    @Alias("含胶量")
    private String rc;
    /**
     * 经纬度尺寸
     */
    @Alias("经*纬")
    private String size;
    /**
     * 标准重量区间最小值
     */
    @Alias("最小值")
    private Double min;
    /**
     * 标准重量区间最大值
     */
    @Alias("最大值")
    private Double max;
    /**
     * 实际重量
     */
    @Alias("实际重量")
    @TableField(value = "acWt")
    private Double acWt;
    /**
     * 验证是否符合标准 OK符合 NG不符合
     **/
    @Alias("检验标准")
    @TableField(value = "checkFlag")
    private String checkFlag;
    /**
     * 创建时间
     */
    @Alias("创建时间")
    private Date createTime;
    /**
     * 开始查询时间
     **/
    @TableField(exist = false)
    private String startTime;
    /**
     * 结束查询时间
     **/
    @TableField(exist = false)
    private String endTime;
}