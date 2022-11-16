package com.wxs.springbootweight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.springbootweight.entity.WeightHome;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【weight_home】的数据库操作Mapper
 * @createDate 2022-10-28 16:10:37
 * @Entity com.wxs.springbootweight.entity.WeightHome
 */

public interface WeightHomeMapper extends BaseMapper<WeightHome> {
    /**
     * description: 查询所有记录
     *
     * @date: 2022/10/31
     * @param: [NULL]
     * @return: java.util.List<com.wxs.springbootweight.entity.WeightHome>
     **/
    List<WeightHome> findAll();

    /**
     * description: 通过多条件动态查询
     *
     * @date: 2022/10/31
     * @param: [gd 工单,pin 料号/pp,startTime 开始查询时间,endTime  结束查询时间]
     * @return: java.util.List<com.wxs.springbootweight.entity.WeightHome>
     **/
    List<WeightHome> findCriteria(Integer pageNum, Integer pageSize, String gd, String pin, String startTime, String endTime);


    /**
     * description:通过ID删除记录
     *
     * @date: 2022/10/31
     * @param: [id]
     * @return: int
     **/
    @Override
    int deleteById(Serializable id);


    List<WeightHome> findPage(Integer pageNum, Integer pageSize, String gd, String pin);

    /**
     * description: 计算历史记录总条数
     *
     * @date: 2022/11/1
     * @param: []
     * @return: java.lang.Integer
     **/
    Integer countTotal();
}




