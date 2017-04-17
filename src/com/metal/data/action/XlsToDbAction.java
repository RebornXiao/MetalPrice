package com.metal.data.action;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.INTERNAL;

import com.metal.data.db.DBHelper;
import com.metal.data.entity.AgData;
import com.metal.data.service.XlsToDbService;

public class XlsToDbAction {

	public static void main(String[] args) {
		/**
		 * 添加数据
		 */
		// addData();
		getModel(5);

	}

	private static void getModel(int num) {
		List<AgData> allByDb = XlsToDbService.getAllByDb();
		System.out.println(allByDb.size() + "数量");
		Double[] change = new Double[num];
		for (int i = 0, size = change.length; i < size; i++) {
			change[i] = allByDb.get(i).getChange_range();
		}
		for (int i = 1, size = allByDb.size(); i < size - change.length; i++) {
			Double[] changeTmp = new Double[num];
			for (int j = 0, leng = changeTmp.length; j < leng; j++) {
				changeTmp[j] = allByDb.get(i + j).getChange_range();
			}
			if (compare(change, changeTmp)) {
				System.out.println("合格的数列：" + allByDb.get(i).getTime());
			}

		}

	}

	private static boolean compare(Double[] data1, Double[] data2) {
		for (int i = 0, leng = data1.length; i < leng; i++) {
			if (Math.abs(data1[i] - data2[i]) > 1) {
				return false;
			}
		}
		return true;
	}

	private static void addData() {
		/*
		 * //得到数据库表中所有的数据 List<AgData> listDb=StuService.getAllByDb();
		 */
		DBHelper db = new DBHelper();
		// 得到表格中所有的数据
		List<AgData> listExcel = XlsToDbService.getAllByExcel("d://ag.xls");
		for (AgData agData : listExcel) {
			// 不存在就添加
			String sql = "insert into t_ag_data (time,open_price,max_price,min_price,close_price,change_type,change_amount,"
					+ "change_range,average_price,turnover,turnover_premium) values(?,?,?,?,?,?,?,?,?,?,?)";
			String[] str = new String[] { agData.getTime() + "", agData.getOpen_price() + "",
					agData.getMax_price() + "", agData.getMin_price() + "", agData.getClose_price() + "",
					agData.getChange_type(), agData.getChange_amount() + "", agData.getChange_range() + "",
					+agData.getAverage_price() + "", agData.getTurnover() + "", agData.getTurnover_premium() + "" };
			db.AddU(sql, str);
		}
	}

	private static void addAgData() {
		// 得到表格中所有的数据
		// 不存在就添加
		AgData agData = new AgData();
		DBHelper db = new DBHelper();
		String sql = "insert into t_ag_data (time,open_price,max_price,min_price,close_price,change_type,change_amount,"
				+ "change_range,average_price,turnover,turnover_premium) values(?,?,?,?,?,?,?,?,?,?,?)";
		String[] str = new String[] { agData.getTime() + "", agData.getOpen_price() + "", agData.getMax_price() + "",
				agData.getMin_price() + "", agData.getClose_price() + "", agData.getChange_type(),
				agData.getChange_amount() + "", agData.getChange_range() + "", +agData.getAverage_price() + "",
				agData.getTurnover() + "", agData.getTurnover_premium() + "" };
		db.AddU(sql, str);
	}

}
