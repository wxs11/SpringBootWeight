package com.wxs.springbootweight.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author:wxs
 * @create: 2022-11-03 13:48
 * @Description: 代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("msg");
//        tables.add("p_question");

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/weight?characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "1234")
                .globalConfig(builder -> {
                    builder.author("wxs")               //作者
                            .outputDir(System.getProperty("user.dir") + "\\src\\main\\java")    //输出路径(写到java目录)
                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd")
                            .fileOverride();            //开启覆盖之前生成的文件

                })
                .packageConfig(builder -> {
                    builder.parent("com.wxs.springbootweight")
//                            .moduleName("practice")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper"));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名 不设置表名则生成全部表
                    builder.addInclude(tables)
                            //  设置过滤表前缀以及表名
                            .addTablePrefix("t_", "sys_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            // 设置实体类策略
                            .entityBuilder()
                            // 开启lombok
                            .enableLombok()
                            // 全局id策略
                            .idType(IdType.AUTO)
                            // 逻辑删除字段名
                            .logicDeleteColumnName("deleted")
                            // 从数据库中生成字段注解
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            // 映射路径使用连字符格式，而不是驼峰
                            .enableHyphenStyle()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            //生成通用的resultMap
                            .enableBaseResultMap()

                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateConfig(new Consumer<TemplateConfig.Builder>() {
                    @Override
                    public void accept(TemplateConfig.Builder builder) {
                        // 实体类使用我们自定义模板
                        builder.entity("templates/entity.java");
                    }
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}