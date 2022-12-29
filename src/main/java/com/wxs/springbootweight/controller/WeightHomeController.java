package com.wxs.springbootweight.controller;

import cn.hutool.core.util.ReUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wxs.springbootweight.entity.ResultVO;
import com.wxs.springbootweight.entity.WeightHome;
import com.wxs.springbootweight.service.WeightHomeService;
import com.wxs.springbootweight.util.ResultUtil;
import com.wxs.springbootweight.util.SapConnect;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xiaoshuai.wei
 */
@RestController
@RequestMapping("/home")
public class WeightHomeController {

    @Resource
    WeightHomeService weightHomeService;

    /**
     * description: 分页动态查询
     * @date: 2022/11/17
     * @param: [pageNum, pageSize, gd, pin, startTime, endTime]
     * @return: com.wxs.springbootweight.entity.ResultVO<com.baomidou.mybatisplus.core.metadata.IPage < com.wxs.springbootweight.entity.WeightHome>>
     **/
    @GetMapping("/findPage")
    public ResultVO<IPage<WeightHome>> findPage(@RequestParam Integer pageNum,
                                                @RequestParam Integer pageSize,
                                                @RequestParam String gd,
                                                @RequestParam String pin,
                                                @RequestParam String ppType,
                                                @RequestParam String startTime,
                                                @RequestParam String endTime) {
        IPage<WeightHome> page = weightHomeService.findPage(pageNum, pageSize, gd, pin,ppType, startTime, endTime);
        return ResultUtil.success(page);
    }

    /**
     * description: 查询所有数据
     * @date: 2022/11/17
     * @param: []
     * @return: com.wxs.springbootweight.entity.ResultVO<java.util.List>
     **/
    @GetMapping("/")
    public ResultVO<List> findAll() {
        List<WeightHome> list = weightHomeService.list();
        return ResultUtil.success(list);
    }

    //未使用 自写分页查询
    @GetMapping("/findCriteria")
    public ResultVO<Map<String, Object>> findCriteria(@RequestParam Integer pageNum,
                                                      @RequestParam Integer pageSize,
                                                      @RequestParam String gd,
                                                      @RequestParam String pin,
                                                      @RequestParam String startTime,
                                                      @RequestParam String endTime) {
        Map<String, Object> page = weightHomeService.findCriteria(pageNum, pageSize, gd, pin, startTime, endTime);
        return ResultUtil.success(page);

    }

    /**
     * description: 新增或修改数据
     *
     * @date: 2022/11/10
     * @param: [weightHome]
     * @return: com.wxs.springbootweight.entity.ResultVO<java.lang.Boolean>
     **/
    @PostMapping("/save")
    public ResultVO<Boolean> save(@RequestBody WeightHome weightHome) {
        boolean re = weightHomeService.saveOrUpdateMsg(weightHome);
        if (re) {
            return ResultUtil.success(re);
        }
        return ResultUtil.error();
    }

    /**
     * description: 根据ID删除记录
     *
     * @date: 2022/11/10
     * @param: [id]
     * @return: com.wxs.springbootweight.entity.ResultVO<java.lang.Boolean>
     **/
    @DeleteMapping("/deleteById/{id}")
    public ResultVO<Boolean> deleteById(@PathVariable int id) {
        Boolean re = weightHomeService.removeById(id);
        if (re) {
            return ResultUtil.success(re);
        }
        return ResultUtil.error();
    }

    /**
     * description:导出
     *
     * @date: 2022/11/10
     * @param: [response]
     * @return: void
     **/
    @GetMapping("/exHistory")
    public void exportMsg(HttpServletResponse response) throws Exception {
        //查询所有数据
        List<WeightHome> list = weightHomeService.list();
        //在内存操作，写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("工单", "工单");
        writer.addHeaderAlias("类型", "类型");
        writer.addHeaderAlias("料号", "料号");
        writer.addHeaderAlias("PP型号", "PP型号");
        writer.addHeaderAlias("含胶量", "含胶量");
        writer.addHeaderAlias("经*纬", "经*纬");
        writer.addHeaderAlias("最小值", "最小值");
        writer.addHeaderAlias("最大值", "最大值");
        writer.addHeaderAlias("实际重量", "实际重量");
        writer.addHeaderAlias("检验标准","检验标准");
        writer.addHeaderAlias("创建时间", "创建时间");

        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        //设置标题
        String fileName = URLEncoder.encode("料号记录历史信息", "UTF-8");
        //Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        //将Writer刷新到OutPut
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();

    }

    /**
     * description: 根据工单获取sap数据
     *
     * @date: 2022/11/10
     * @param: [gd] 工单
     * @return: com.wxs.springbootweight.entity.ResultVO
     **/
    @GetMapping("/sap")
    public ResultVO sap(@RequestParam String gd) {
        String res;
        String  match = ReUtil.replaceAll(gd,"#","");
        SapConnect sapConnect = new SapConnect();
        if (match.indexOf("-") > -1) {
            res = match.split("-")[0];
        } else {
            res = match;
        }

        HashMap<String, String> map = sapConnect.doPost(res);
        if (map == null) {
            return ResultUtil.error();
        }
        return ResultUtil.success(map);
    }
}
