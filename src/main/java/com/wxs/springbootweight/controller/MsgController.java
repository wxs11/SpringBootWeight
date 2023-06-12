package com.wxs.springbootweight.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wxs.springbootweight.entity.Msg;
import com.wxs.springbootweight.entity.ResultVO;
import com.wxs.springbootweight.service.MsgService;
import com.wxs.springbootweight.util.MinGeSerialTest;
import com.wxs.springbootweight.util.ResultUtil;
import com.wxs.springbootweight.util.excel.ExcelUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wxs
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/msg")
public class MsgController {
    int flag = 0;

    @Resource
    MsgService msgService;


    @GetMapping("/list")
    public ResultVO<IPage<Msg>> getList(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam String ppType) {
        IPage<Msg> page = msgService.getList(pageNum, pageSize, ppType);
        if (page == null) {
            return ResultUtil.error();
        }
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
    public ResultVO<Boolean> save(@RequestBody Msg msg) {
        boolean re = msgService.saveOrUpdateMsg(msg);
        if (re) {
            return ResultUtil.success(true);
        }
        return ResultUtil.error();
    }

    @PostMapping("/saveOne")
    public ResultVO saveOne(@RequestBody Msg msg) {
        int re = msgService.saveOne(msg);
        if (re == 0) {
            return ResultUtil.error();
        }
        return ResultUtil.success(re);
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
        Boolean re = msgService.removeById(id);
        if (re) {
            return ResultUtil.success(true);
        }
        return ResultUtil.error();
    }

    /**
     * description: 根据尺寸获取PP型号下拉框数据
     *
     * @date: 2022/11/10
     * @param: [size]
     * @return: com.wxs.springbootweight.entity.ResultVO<java.util.List>
     **/
    @GetMapping("/getPPTypes")
    public ResultVO<List> getPPTypes(@RequestParam String size) {
        List<String> maps = msgService.getPPTypes(size);
        if (maps.size() > 0) {
            return ResultUtil.success(maps);
        }
        return ResultUtil.error();
    }

    @GetMapping("/getRcs")
    public ResultVO<List> getRcs(@RequestParam String ppType,
                                 @RequestParam String size) {
        List<String> list = msgService.getRcs(ppType, size);
        if (list.size() > 0) {
            return ResultUtil.success(list);
        }
        return ResultUtil.error();
    }

    /**
     * description: 获取体重秤实际重量
     *
     * @date: 2022/11/11
     * @param: []
     * @return: com.wxs.springbootweight.entity.ResultVO
     **/
    @GetMapping("/getAcWt")
    public ResultVO getAcWt() {
        MinGeSerialTest handle = new MinGeSerialTest();
        int res = 0;
        if (flag == 0) {
            int i = handle.init();
            if (i == 1) {
                handle.start();
                res = MinGeSerialTest.acWt;
                flag = 1;
            }
        } else if (flag == 1) {
            res = MinGeSerialTest.acWt;
        }

        return ResultUtil.success(res);
    }


    /**
     * description: 根据id查询标准重量区间
     *
     * @date: 2022/11/10
     * @param: [id]
     * @return: java.util.List<com.wxs.springbootweight.entity.Msg>
     **/
    @GetMapping("/getBzWt")
    public ResultVO getBzWt(@RequestParam String ppType,
                            @RequestParam String rc,
                            @RequestParam String size) {
        List<Msg> list = msgService.getBzWt(ppType, rc, size);
        if (list.size() > 0) {
            return ResultUtil.success(list);
        }
        return ResultUtil.error();
    }

    /**
     * description:导出
     *
     * @date: 2022/11/16
     * @param: [response]
     * @return: void
     **/
    @GetMapping("/exMsg")
    public void exportMsg(HttpServletResponse response) throws Exception {
        //查询所有数据
        List<Msg> list = msgService.list();
        //在内存操作，写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("称重类型", "称重类型");
        writer.addHeaderAlias("PP型号/料号", "PP型号/料号");
        writer.addHeaderAlias("经*纬", "经*纬");
        writer.addHeaderAlias("含胶量", "含胶量");
        writer.addHeaderAlias("最小值", "最小值");
        writer.addHeaderAlias("最大值", "最大值");

        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        //设置标题
        String fileName = URLEncoder.encode("PP/料号资料表", "UTF-8");
        //Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        //将Writer刷新到OutPut
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();

    }

    /**
     * description: 导入
     *
     * @date: 2022/11/16
     * @param: [file]
     * @return: com.wxs.springbootweight.entity.ResultVO<java.lang.Boolean>
     **/
    @PostMapping("/import")
    public ResultVO<Boolean> impMsg(@RequestParam MultipartFile file) throws IOException {
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Msg> list = reader.readAll(Msg.class);
        try {
            Boolean res = msgService.saveBatch(list);
            return ResultUtil.success(res);
        } catch (Exception e) {
            return ResultUtil.error();
        }

        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<WeightHome> weightHomeList = CollUtil.newArrayList();
//        try{
//            for (List<Object> row : list) {
//                WeightHome weightHomes = new WeightHome();
//                weightHomes.setGd(row.get(1).toString());
//                weightHomes.setType(row.get(2).toString());
//                weightHomes.setPin(row.get(3).toString());
//                weightHomes.setRc(row.get(4).toString());
//                weightHomes.setSize((row.get(5).toString()));
//                weightHomes.setMin(Double.valueOf(row.get(6).toString()));
//                weightHomes.setMax(Double.valueOf(row.get(7).toString()));
//                weightHomes.setAcWt(Double.valueOf(row.get(8).toString()));
//                weightHomeList.add(weightHomes);
//            }
//            System.out.println(weightHomeList);
//            weightHomeService.saveBatch(weightHomeList);
//            return  ResultUtil.success(weightHomeList);
//        }catch (Exception e){
//           return ResultUtil.error();
//        }
    }

    @PostMapping("/importPP")
    public ResultVO<Boolean> impPP(@RequestParam MultipartFile file) throws Exception {
        TimeInterval timer = DateUtil.timer();
//        读取的Excel表数据
        List<Msg> listRead = ExcelUtils.readMultipartFile(file, Msg.class);
//        数据库的全部数据
        List<Msg> listAll = msgService.list();
//        保存需要导入的数据
        List<Msg> resList = new ArrayList<>();
        boolean a;
        for (Msg msg : listRead) {
            if ("1".equals(msg.getType())) {
                a = listAll.stream().anyMatch(m -> m.getPpType().equals(msg.getPpType())
                        && m.getType().equals(msg.getType())
                        && m.getRc().equals(msg.getRc())
                        && m.getSize().equals(msg.getSize())
                        && m.getMin().equals(msg.getMin())
                        && m.getMax().equals(msg.getMax()));
                //如果数据不存在则需导入
            } else {
                a = listAll.stream().anyMatch(m -> m.getPpType().equals(msg.getPpType())
                        && m.getType().equals(msg.getType())
                        && m.getMin().equals(msg.getMin())
                        && m.getMax().equals(msg.getMax()));
                //如果数据不存在则需导入
            }
            if (!a) {
                resList.add(msg);
            }
        }
        boolean res;
        try{
            if (resList.size() > 0) {
                res = msgService.insertBatch(resList);
                System.out.println("一共花费时间:" + timer.interval());
                return ResultUtil.success(res);
            }else {
                return ResultUtil.success(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultUtil.success(true);
    }

}
