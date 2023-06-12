package com.wxs.springbootweight.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxs.springbootweight.entity.Msg;

import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【msg】的数据库操作Service
 * @createDate 2022-11-09 10:25:42
 */
public interface MsgService extends IService<Msg> {


     IPage<Msg> getList(Integer pageNum, Integer pageSIze, String ppType);

     boolean saveOrUpdateMsg(Msg msg);

//     List getPPMaps(String size);

     List<String> getPPTypes(String size);

     List<String> getRcs(String ppType,String size);

     List<Msg> getBzWt(String ppType, String rc,String size);

     int saveOne(Msg msg);

     boolean insertBatch(List<Msg> list);
}
