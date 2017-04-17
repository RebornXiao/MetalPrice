package com.metal.data.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.omg.PortableServer.AdapterActivator;

import com.metal.data.db.DBHelper;
import com.metal.data.entity.AgData;
import com.mysql.fabric.xmlrpc.base.Data;

import jxl.Sheet;
import jxl.Workbook;

public class XlsToDbService {
	/**
	 * ��ѯstu�������е�����
	 * 
	 * @return
	 */
	public static List<AgData> getAllByDb() {
		List<AgData> list = new ArrayList<AgData>();
		try {
			DBHelper db = new DBHelper();
			String sql = "select * from t_ag_data";
			ResultSet rs = db.Search(sql, null);
			while (rs.next()) {
				int id = rs.getInt("id");
				String time = rs.getString("time");
				Integer open_price = rs.getInt("open_price");
				Integer max_price = rs.getInt("max_price");
				Integer min_price = rs.getInt("open_price");
				Integer close_price = rs.getInt("close_price");
				Integer change_amount = rs.getInt("change_amount");
				String change_type = rs.getString("change_type");
				Double change_range = rs.getDouble("change_range");
				Integer average_price = rs.getInt("average_price");
				Integer turnover = rs.getInt("turnover");
				Long turnover_premium = rs.getLong("turnover_premium");
				
				AgData agData = new AgData();
				agData.setId(id);
				agData.setTime(time);
				agData.setOpen_price(open_price);
				agData.setMax_price(max_price);
				agData.setMin_price(min_price);
				agData.setClose_price(close_price);
				agData.setChange_amount(change_amount);
				agData.setChange_type(change_type);
				agData.setChange_range(change_range);
				agData.setAverage_price(average_price);
				agData.setTurnover(turnover);
				agData.setTurnover_premium(turnover_premium);

				list.add(agData);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ѯָ��Ŀ¼�е��ӱ�������е�����
	 * 
	 * @param file
	 *            �ļ�����·��
	 * @return
	 */
	public static List<AgData> getAllByExcel(String file) {
		List<AgData> list = new ArrayList<AgData>();
		try {
			Workbook rwb = Workbook.getWorkbook(new File(file));
			Sheet rs = rwb.getSheet(0);// ����rwb.getSheet(0)
			int clos = rs.getColumns();// �õ����е���
			int rows = rs.getRows();// �õ����е���

			System.out.println(clos + " rows:" + rows);
			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < clos; j++) {
					// ��һ�����������ڶ���������
					String date = rs.getCell(j++, i).getContents();// ����//Ĭ������߱��Ҳ��һ��
																	// ���������j++
					String name = rs.getCell(j++, i).getContents();// ��Լ����
					String open = rs.getCell(j++, i).getContents();// ���̼�
					String max = rs.getCell(j++, i).getContents();// ��߼�
					String min = rs.getCell(j++, i).getContents();// ��ͼ�
					String close = rs.getCell(j++, i).getContents();// ���̼�
					String change = rs.getCell(j++, i).getContents();// �ǵ���
					String change_range = rs.getCell(j++, i).getContents();// �ǵ�����
					String average = rs.getCell(j++, i).getContents();// ��Ȩƽ����
					String turnover = rs.getCell(j++, i).getContents();// �ɽ�������
					String turnover_money = rs.getCell(j++, i).getContents();// �ɽ����
					AgData agData = new AgData();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date time = sdf.parse(date);
					agData.setTime(date);

					double openDou = Double.parseDouble(open);
					Integer open_price = Integer.valueOf((int) (openDou));
					agData.setOpen_price(open_price);

					double maxDou = Double.parseDouble(max);
					Integer max_price = Integer.valueOf((int) (maxDou));
					agData.setMax_price(max_price);

					double minDou = Double.parseDouble(min);
					Integer min_price = Integer.valueOf((int) (minDou));
					agData.setMin_price(min_price);

					double closeDou = Double.parseDouble(close);
					Integer close_price = Integer.valueOf((int) (closeDou));
					agData.setClose_price(close_price);

					if (change.startsWith("-")) {
						change = change.replace("-", "");
					}
					double changeDou = Double.parseDouble(change);
					Integer change_price = Integer.valueOf((int) (changeDou));
					agData.setChange_amount(change_price);

					String changeType = "+";

					String changeStr = change_range.replace("%", "");
					if (changeStr.startsWith("-")) {
						changeType = "-";
					}

					if (changeStr.equals("--")) {
						agData.setChange_range(0d);
						agData.setChange_type("+");
					} else {
						double changDou = Double.parseDouble(changeStr);
						agData.setChange_range(changDou);
						agData.setChange_type(changeType);
					}

					if (average.equals("--")) {
						agData.setAverage_price(0);
					} else {
						double avergeDou = Double.parseDouble(average);
						Integer avergeInt = Integer.valueOf((int) avergeDou);
						agData.setAverage_price(avergeInt);
					}

					if (turnover.equals("--")) {
						agData.setTurnover(0);
					} else {
						double turnoverDou = Double.parseDouble(turnover);
						Integer turnoverInt = Integer.valueOf((int) (turnoverDou));
						agData.setTurnover(turnoverInt);
					}

					if (turnover_money.equals("--")) {
						agData.setTurnover_premium(0L);
					} else {
						double turnoverMoneyDou = Double.parseDouble(turnover_money);
						Long turnoverLong = Long.valueOf((long) (turnoverMoneyDou));
						agData.setTurnover_premium(turnoverLong);
					}
					list.add(agData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * ͨ��Id�ж��Ƿ����
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isExist(int id) {
		try {
			DBHelper db = new DBHelper();
			ResultSet rs = db.Search("select * from stu where id=?", new String[] { id + "" });
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		/*
		 * List<AgData> all=getAllByDb(); for (AgData AgData : all) {
		 * System.out.println(AgData.toString()); }
		 */

		System.out.println(isExist(1));

	}
}
