package com.wxs.springbootweight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.springbootweight.entity.Msg;

import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【msg】的数据库操作Mapper
 * @createDate 2022-11-09 10:25:42
 * @Entity com.wxs.springbootweight.entity.Msg
 */
public interface MsgMapper extends BaseMapper<Msg> {

    /**
     * description: 根据尺寸获取PP型号下拉框数据
     *
     * @date: 2022/11/10
     * @param: [size] 尺寸
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     **/
    List<String> getPPTypes(String size);
    //    List<Map<String, Object>> getPPMaps(String size);

    /**
     * description: 根据ppType获取对应类型的rc值
     * @date: 2022/12/7
     * @param: [ppType]
     * @return: java.util.List<java.lang.String>
     **/
    List<String> getRcs(String ppType ,String size);

    /**
     * description: 根据id查询标准重量区间
     *
     * @date: 2022/11/10
     * @param: [id] id
     * @return: java.util.List<com.wxs.springbootweight.entity.Msg>
     **/
    List<Msg> getBzWt(String ppType, String rc,String size);
    /**
     * description: 批量插入数据
     * @date: 2022/12/13
     * @param: [list]
     * @return: java.util.List<com.wxs.springbootweight.entity.Msg>
     **/

    List<Msg> insertImp(List<Msg> list);
    /**
     * description: 新增一条数据库没有的数据 如果有则不插入
     * @date: 2022/12/13
     * @param: [msg] msg实体
     * @return: int
     **/
    int saveOne(Msg msg);


}




