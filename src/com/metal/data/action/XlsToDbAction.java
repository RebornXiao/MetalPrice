package com.metal.data.action;

import java.util.ArrayList;
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
		 * �������
		 */
		// addData();
		getModel(5);

	}

	private static void getModel(int num) {
		List<AgData> allByDb = XlsToDbService.getAllByDb();
		System.out.println(allByDb.size() + "����");
		Double[] change = new Double[num];
		List<AgData> listTemp = new ArrayList<>();
		for (int k = 0; k < 10; k++) {
			listTemp.add(allByDb.get(k));
		}
		System.out.println("���10������:��---->"+isUp(listTemp));
		for (int i = 0, size = change.length; i < size; i++) {
			change[i] = allByDb.get(i+10).getChange_range();
		}
		for (int i = 1, size = allByDb.size(); i < size - change.length; i++) {
			Double[] changeTmp = new Double[num];
			for (int j = 0, leng = changeTmp.length; j < leng; j++) {
				changeTmp[j] = allByDb.get(i + j).getChange_range();
			}
			if (compare(change, changeTmp)) {
				System.out.println("�ϸ�����У�" + allByDb.get(i).getTime() + ";price=" + allByDb.get(i).getAverage_price()
						+ ";id=" + allByDb.get(i).getId());
				System.out.println("���ݾ��ۣ�" + allByDb.get(i - 1).getAverage_price().toString() + ";"
						+ allByDb.get(i - 2).getAverage_price().toString() + ";"
						+ allByDb.get(i - 3).getAverage_price().toString() + ";"
						+ allByDb.get(i - 4).getAverage_price().toString() + ";"
						+ allByDb.get(i - 5).getAverage_price().toString() + ";");
				List<AgData> list = new ArrayList<>();
				for (int k = 0; k < 5; k++) {
					list.add(allByDb.get(i-k));
				}
				System.out.println("����:��---->"+isUp(list));

			}
		}
	}

	private static boolean isUp(List<AgData> agDatas) {
		int add = 0;
		for (int i = 0, size = agDatas.size(); i < size; i++) {
			if (agDatas.get(i).getChange_type().equals("+")) {
				add++;
			} else {
				add--;
			}
		}
		if (add > 0) {
			return true;
		} else {
			return false;
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
		 * //�õ����ݿ�������е����� List<AgData> listDb=StuService.getAllByDb();
		 */
		DBHelper db = new DBHelper();
		// �õ���������е�����
		List<AgData> listExcel = XlsToDbService.getAllByExcel("d://ag.xls");
		for (AgData agData : listExcel) {
			// �����ھ����
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
		// �õ���������е�����
		// �����ھ����
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
