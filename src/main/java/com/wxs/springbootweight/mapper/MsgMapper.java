package com.wxs.springbootweight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.springbootweight.entity.Msg;

import java.util.List;
import java.util.Map;

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
    List<Map<String, Object>> getPPMaps(String size);


    /**
     * description: 根据id查询标准重量区间
     *
     * @date: 2022/11/10
     * @param: [id] id
     * @return: java.util.List<com.wxs.springbootweight.entity.Msg>
     **/
    List<Msg> getBzWt(Integer id, String pin);
}




