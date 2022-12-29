package com.wxs.springbootweight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxs.springbootweight.entity.Msg;
import com.wxs.springbootweight.mapper.MsgMapper;
import com.wxs.springbootweight.service.MsgService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【msg】的数据库操作Service实现
 * @createDate 2022-11-09 10:25:42
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg>
        implements MsgService {

    @Resource
    MsgMapper msgMapper;


    @Override
    public IPage<Msg> getList(Integer pageNum, Integer pageSIze, String ppType) {
        IPage<Msg> page = new Page<>(pageNum, pageSIze);
        QueryWrapper<Msg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(Strings.isNotEmpty(ppType), Msg::getPpType, ppType);
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean saveOrUpdateMsg(Msg msg) {
        return this.saveOrUpdate(msg);
    }

    @Override
    public List<String> getPPTypes(String size) {
        List<String> list = msgMapper.getPPTypes(size);
        return list;
    }

    @Override
    public List<String> getRcs(String ppType ,String size) {
        return msgMapper.getRcs(ppType,size);
    }

    @Override
    public List<Msg> getBzWt(String ppType, String rc, String size) {
        return msgMapper.getBzWt(ppType,rc,size);
    }

    @Override
    public int saveOne(Msg msg) {
        return msgMapper.saveOne(msg);
    }

//    @Override
//    public List getPPMaps(String size) {
//        List<Map<String, Object>> list = msgMapper.getPPMaps(size);
//        List res = new ArrayList<>();
//        String pp;
//        for (int i = 0; i < list.size(); i++) {
//            pp = list.get(i).get("ppType") + "-" + list.get(i).get("rc") + "+" + list.get(i).get("id");
//            res.add(pp);
//        }
//
//        return res;
//    }

}




