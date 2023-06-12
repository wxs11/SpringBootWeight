package com.wxs.springbootweight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxs.springbootweight.entity.WeightHome;
import com.wxs.springbootweight.mapper.WeightHomeMapper;
import com.wxs.springbootweight.service.WeightHomeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoshuai.wei
 * description 针对表【weight_home】的数据库操作Service实现
 * @createDate 2022-10-28 16:10:37
 */
@Service
public class WeightHomeServiceImpl extends ServiceImpl<WeightHomeMapper, WeightHome>
        implements WeightHomeService {

    @Resource
    WeightHomeMapper weightHomeMapper;


    @Override
    public List<WeightHome> findAll() {
        return weightHomeMapper.findAll();
    }


    @Override
    public boolean saveOrUpdateMsg(WeightHome weightHome) {
        return this.saveOrUpdate(weightHome);
    }


    @Override
    public IPage<WeightHome> findPage(Integer pageNum, Integer pageSize, String gd, String pin,String ppType,  String startTime, String endTime) {
        IPage<WeightHome> page = new Page<>(pageNum, pageSize);
        QueryWrapper<WeightHome> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().like(Strings.isNotEmpty(gd), WeightHome::getGd, gd)
                .like(Strings.isNotEmpty(pin), WeightHome::getPin, pin)
                .like(Strings.isNotEmpty(ppType),WeightHome::getPpType,ppType)
                .between(Strings.isNotEmpty(startTime), WeightHome::getCreateTime, startTime, endTime);
        queryWrapper1.orderByDesc("create_time");
        return this.page(page, queryWrapper1);

    }

    @Override
    public Integer countTotal() {
        return weightHomeMapper.countTotal();
    }

    @Override
    public Map<String, Object> findCriteria(Integer pageNum, Integer pageSize, String gd, String pin, String startTime, String endTime) {
        pageNum = (pageNum - 1) * pageSize;
        List<WeightHome> list = weightHomeMapper.findCriteria(pageNum, pageSize, gd, pin, startTime, endTime);
//        int total = weightHomeMapper.countTotal();
        int total = list.size();
        Map<String, Object> res = new HashMap<>();
        res.put("records", list);
        res.put("total", total);
        return res;
    }
}




