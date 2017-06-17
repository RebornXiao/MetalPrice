package com.metal.data.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.INTERNAL;
import org.w3c.dom.traversal.NodeIterator;

import com.metal.data.db.DBHelper;
import com.metal.data.entity.AgData;
import com.metal.data.service.XlsToDbService;

public class XlsToDbAction {

	public static void main(String[] args) {
		/**
		 * 添加数据
		 */
		// addData();
		// getModel(10);
		// getNum();
		// getPeriod();
		// add5minData();
		// add1hData();

		test(50);
	}

	private static void test(int price) {
		List<AgData> allByDb = XlsToDbService.getAllByDb();
		System.out.println(allByDb.size() + "数量");
		int y = allByDb.get(allByDb.size() - 1).getClose_price();// 买入价
		int buy = 1;// 买入份额
		int buyMax = 0;
		int add = 0; // 盈利

		int buySize = 1;// 买入次数
		int sailSize = 0;// 卖出次数
		for (int size = allByDb.size(), i = size - 1; i > 0; i--) {
			int x = allByDb.get(i).getClose_price();// 每次收盘价

			/**
			 * 情况：1.买入价在最高价和最低价之间 2.卖出价在最高价和最低价之间
			 * 
			 * 3.买入后， 遇到新价格。1.确定他的买入价，确定他的卖出价。
			 */
			int buyPriceTmp = y - price;// (补仓价)
			int salePriceTmp = y + price;// (盈利价)
			int minPriceTmp = allByDb.get(i).getMin_price();
			int maxPriceTmp = allByDb.get(i).getMax_price();

			System.out.println(allByDb.get(i).getTime() + " 本日最低价=" + minPriceTmp + ";" + "本日最高价=" + maxPriceTmp);
			if (maxPriceTmp >= salePriceTmp && buy > 0) {// 上涨50点
				// 交易，賣出
				add = add + price * buy;// 利潤
				System.out.println("当前均价" + y);
				System.out.println(allByDb.get(i).getTime() + ":卖出盈利=" + price * buy);
				buy = 0;
				y = salePriceTmp - 200;
				sailSize++;
			} else if (minPriceTmp <= buyPriceTmp && buy > 0) {// 下降50点
				// 交易,买入
				int buyNum = 0;
				if ((y - minPriceTmp) / price == 1) {// 下降了50点
					int buytemp = buy;// 之前的老数量
					buy++;
					buyNum = 1;
					y = (y * buytemp + (y - price)) / buy;
					if (buy > buyMax) {
						buyMax = buy;
					}
				} else {// 下降了50的倍数
					int buytemp = buy;// 之前的老数量
					buy = buy + (y - minPriceTmp) / price;
					if (buy > buyMax) {
						buyMax = buy;
					}
					buyNum = buy - buytemp;
					// y = (y + y - ((y - x) / 50) * 50) * buy / 2;
					y = (y * buytemp
							+ (2 * y - price - ((y - minPriceTmp) / price) * price) * ((y - minPriceTmp) / price) / 2)
							/ buy;
				}
				System.out.println(allByDb.get(i).getTime() + "买入" + buyNum);
				System.out.println("当前均价" + y);
				buySize = buySize + buyNum;
			} else if (minPriceTmp - y <= 0 && buy == 0) {
				buy = 1;
				System.out.println(allByDb.get(i).getTime() + "买入" + 1);
				System.out.println("当前均价" + y);
				buySize++;
			} else {// 波动不足50点
				System.out.println(allByDb.get(i).getTime() + ":本日无操作");
				System.out.println("当前均价" + y);
			}

		}
		System.out.println("最终收益 ：" + add);
		System.out.println("买次数 ：" + buySize);
		System.out.println("卖次数 ：" + sailSize);
		System.out.println("买最大次数 ：" + buyMax);
	}

	private static void getPeriod() {
		List<AgData> allByDb = XlsToDbService.getAll1hByDb();
		System.out.println(allByDb.size() + "数量");
		// 根据前一天上下，入室买入，然后判断最高点有没有大于4点到5点
		int numadd = 0;
		int numAll = 0;

		int numdown = 0;
		int numDall = 0;
		try {
			for (int i = 0, size = allByDb.size(); i < size; i++) {
				AgData agData = allByDb.get(i);
				AgData agData2 = allByDb.get(i + 1);
				// AgData agData3 = allByDb.get(i + 2);
				// AgData agData4 = allByDb.get(i + 3);
				// AgData agData5 = allByDb.get(i + 4);
				// AgData agData6 = allByDb.get(i + 5);
				// AgData agData7 = allByDb.get(i + 6);
				// AgData agData8 = allByDb.get(i + 7);
				// AgData agData9 = allByDb.get(i + 8);
				// AgData agData10 = allByDb.get(i + 9);
				List<AgData> agDatas = new ArrayList<>();
				agDatas.clear();
				agDatas.add(agData2);
				// agDatas.add(agData3);
				// agDatas.add(agData4);
				// agDatas.add(agData5);
				// agDatas.add(agData6);
				// agDatas.add(agData7);
				// agDatas.add(agData8);
				// agDatas.add(agData9);
				// agDatas.add(agData10);
				if (agData.getChange_type().equals("+")) {
					numAll++;
					if (isAdd(agDatas, agData, 5)) {
						numadd++;
					}
				} else {
					numDall++;
					if (isDown(agDatas, agData, 5)) {
						numdown++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("上:" + numadd + ";总数：" + numAll);
			System.out.println("下：" + numdown + ";总数：" + numDall);
		}

	}

	private static boolean isAdd(List<AgData> agDatas, AgData agData, int num) {
		boolean isOk = false;
		for (AgData agData2 : agDatas) {
			if ((agData2.getMax_price() - agData.getOpen_price()) > num) {
				isOk = true;
			}
		}
		return isOk;
	}

	private static boolean isDown(List<AgData> agDatas, AgData agData, int num) {
		boolean isOk = false;
		for (AgData agData2 : agDatas) {
			if ((agData.getOpen_price() - agData.getMin_price()) > num) {
				isOk = true;
			}
		}
		return isOk;
	}

	private static boolean isOk(AgData agData1, AgData agData2) {
		boolean isOk = false;
		if ((agData1.getMax_price() - agData2.getOpen_price()) > 3) {
			return isOk;
		}
		return isOk;
	}

	private static void getNum() {
		List<AgData> allByDb = XlsToDbService.getAllByDb();
		System.out.println(allByDb.size() + "数量");
		int num = 0;
		for (int i = 0, size = allByDb.size(); i < size; i++) {
			AgData agData = allByDb.get(i);
			if (agData.getOpen_price() < agData.getMax_price() && agData.getClose_price() > agData.getMin_price()) {
				num++;
			}
		}
		int numS = 0;
		for (int i = 0, size = allByDb.size(); i < size; i++) {
			AgData agData = allByDb.get(i);
			if (agData.getClose_price() < agData.getMax_price() && agData.getClose_price() > agData.getMin_price()) {
				numS++;
			}
		}
		System.out.println("开盘价:" + num + ";" + (double) (((double) num) / (double) (allByDb.size())));
		System.out.println("收盘价:" + numS + ";" + (double) (((double) numS) / (double) (allByDb.size())));
	}

	private static void getModel(int num) {
		List<AgData> allByDb = XlsToDbService.getAllByDb();
		System.out.println(allByDb.size() + "数量");
		Double[] change = new Double[num];
		List<AgData> listTemp = new ArrayList<>();
		for (int k = 0; k < 10; k++) {
			listTemp.add(allByDb.get(k));
		}
		System.out.println("最近10天行情:上---->" + isUp(listTemp));
		for (int i = 0, size = change.length; i < size; i++) {
			change[i] = allByDb.get(i + 10).getChange_range();
		}
		for (int i = 1, size = allByDb.size(); i < size - change.length; i++) {
			Double[] changeTmp = new Double[num];
			for (int j = 0, leng = changeTmp.length; j < leng; j++) {
				changeTmp[j] = allByDb.get(i + j).getChange_range();
			}
			if (compare(change, changeTmp)) {
				System.out.println("合格的数列：" + allByDb.get(i).getTime() + ";price=" + allByDb.get(i).getAverage_price()
						+ ";id=" + allByDb.get(i).getId());
				System.out.println("数据均价：" + allByDb.get(i - 1).getAverage_price().toString() + ";"
						+ allByDb.get(i - 2).getAverage_price().toString() + ";"
						+ allByDb.get(i - 3).getAverage_price().toString() + ";"
						+ allByDb.get(i - 4).getAverage_price().toString() + ";"
						+ allByDb.get(i - 5).getAverage_price().toString() + ";");
				List<AgData> list = new ArrayList<>();
				for (int k = 0; k < 5; k++) {
					list.add(allByDb.get(i - k));
				}
				System.out.println("行情:上---->" + isUp(list));

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

	private static void add5minData() {
		/*
		 * //得到数据库表中所有的数据 List<AgData> listDb=StuService.getAllByDb();
		 */
		DBHelper db = new DBHelper();
		// 得到表格中所有的数据
		List<AgData> listExcel = XlsToDbService.getAll5MinByExcel("d://ag_5min.xls");
		for (AgData agData : listExcel) {
			// 不存在就添加
			String sql = "insert into t_ag_data_5min (time,open_price,max_price,min_price,close_price,change_type,change_amount,"
					+ "change_range,average_price,turnover,turnover_premium) values(?,?,?,?,?,?,?,?,?,?,?)";
			String[] str = new String[] { agData.getTime() + "", agData.getOpen_price() + "",
					agData.getMax_price() + "", agData.getMin_price() + "", agData.getClose_price() + "",
					agData.getChange_type(), agData.getChange_amount() + "", agData.getChange_range() + "",
					+agData.getAverage_price() + "", agData.getTurnover() + "", agData.getTurnover_premium() + "" };
			db.AddU(sql, str);
		}
	}

	private static void add1hData() {
		/*
		 * //得到数据库表中所有的数据 List<AgData> listDb=StuService.getAllByDb();
		 */
		DBHelper db = new DBHelper();
		// 得到表格中所有的数据
		List<AgData> listExcel = XlsToDbService.getAll1hByExcel("d://ag_1h.xls");
		for (AgData agData : listExcel) {
			// 不存在就添加
			String sql = "insert into t_ag_data_1h (time,open_price,max_price,min_price,close_price,change_type,change_amount,"
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
