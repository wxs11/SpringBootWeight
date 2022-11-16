package com.wxs.springbootweight.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxs.springbootweight.entity.WeightHome;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoshuai.wei
 * @description 针对表【weight_home】的数据库操作Service
 * @createDate 2022-10-28 16:10:37
 */
public interface WeightHomeService extends IService<WeightHome> {


    List<WeightHome> findAll();

    boolean saveOrUpdateMsg(WeightHome weightHome);


    IPage<WeightHome> findPage(Integer pageNum, Integer pageSize, String gd, String pin, String startTime, String endTime);

    Integer countTotal();

    Map<String, Object> findCriteria(Integer pageNum, Integer pageSize, String gd, String pin, String startTime, String endTime);


}
