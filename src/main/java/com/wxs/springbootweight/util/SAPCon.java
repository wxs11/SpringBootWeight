package com.wxs.springbootweight.util;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 与SAP连接配置
 *
 * @author jay
 */
public class SAPCon {

    private static final String ABAP_AS_POOLED = "ZOA_WIP_INFO";

    static {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,
                "172.16.200.18");// 服务器
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "30"); // 系统编号
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
                "100"); // SAP集团
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,
                "GD-SERVICE"); // SAP用户名 GUOXJ
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
                "Service123"); // 密码 a44444
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "ZH"); // 登录语言:ZH EN
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3"); // 最大连接数
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10"); // 最大连接线程
        connectProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER,
                ""); // sap router字符串

        createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);
    }




    private static void createDataFile(String name, String suffix, Properties properties) {
        File cfg = new File(name + "." + suffix);
        if (cfg.exists()) {
            cfg.deleteOnExit();
        }
        try {
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "for tests only !");
            fos.close();
        } catch (Exception e) {
            System.out.println("Create Data file fault, error msg: " + e.toString());
            throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
        }
    }

    /*
     * * 获取SAP连接
     *
     * @return SAP连接对象
     */
    public static JCoDestination connect() {
        JCoDestination destination = null;
        try {
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        } catch (JCoException e) {
            System.out.println("Connect SAP fault, error msg: " + e);
        }
        return destination;
    }
}