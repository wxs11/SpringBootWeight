package com.wxs.springbootweight.util;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import java.util.HashMap;

/**
 * @author xiaoshuai.wei
 */
@SuppressWarnings("unused")
public class SapConnect {

	public HashMap<String, String> doPost(String gd) {
		JCoFunction function = null;
		JCoDestination destination = SAPCon.connect();
		int result = 0;// 调用接口返回状态
		String message = "";// 调用接口返回信息
//		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			// 调用ZCHENH001函数
			function = destination.getRepository()
					.getFunction("ZOA_WIP_INFO");
			JCoParameterList input = function.getImportParameterList();
			input.setValue("WERKS", "9999");
			input.setValue("WCENT", "其他");
			input.setValue("AUFNR", "1345");
			function.execute(destination); // 执行连接


//			result = function.getExportParameterList().getInt("RETURN");// 调用接口返回结果
//			message = function.getExportParameterList().getString("MESSAGE");// 调用接口返回信息


			JCoTable codes = function.getTableParameterList().getTable("GT_CONF");
			HashMap<String, String> hm = new HashMap<String, String>();
			if (hm.size() > 0) {
				hm.put("MATNR", codes.getString("MATNR"));
				hm.put("PNLSIZE", codes.getString("PNLSIZE"));
				String pin = hm.get("MATNR");
				String size = hm.get("PNLSIZE");
				System.out.println(
						"Pin--->" + pin + ";size--->" + size);
				return hm;
			} else {
				return null;
			}


		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

}
