package com.metal.data.action;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.metal.data.db.DBHelper;
import com.metal.data.entity.AgData;
import com.metal.data.service.XlsToDbService;

public class Work implements Runnable {

	ExecutorService executorService = Executors.newFixedThreadPool(4);  
	DBHelper db = new DBHelper();
	AgData agData;

	// 得到表格中所有的数据
	List<AgData> listExcel ;
	
	public Work(ExecutorService executorService, DBHelper db, AgData agData, List<AgData> listExcel) {
		super();
		this.executorService = executorService;
		this.db = db;
		this.agData = agData;
		this.listExcel = listExcel;
	}

	@Override
	public void run() {
		String sql = "insert into t_ag_data (time,open_price,max_price,min_price,close_price,change_type,change_amount,"
				+ "change_range,average_price,turnover,turnover_premium) values(?,?,?,?,?,?,?,?,?,?,?)";
		String[] str = new String[] { agData.getTime() + "", agData.getOpen_price() + "",
				agData.getMax_price() + "", agData.getMin_price() + "", agData.getClose_price() + "",
				agData.getChange_type(), agData.getChange_amount() + "", agData.getChange_range() + "",
				+agData.getAverage_price() + "", agData.getTurnover() + "", agData.getTurnover_premium() + "" };
		db.AddU(sql, str);
			
	}

}
