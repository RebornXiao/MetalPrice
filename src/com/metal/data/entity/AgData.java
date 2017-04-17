package com.metal.data.entity;


public class AgData {
	private Integer id ;//int(4) //NOT NULL AUTO_INCREMENT,
	private String time;
	private Integer open_price ;//int(4) DEFAULT NULL,
	private Integer  max_price ;//int(4) DEFAULT NULL,
	private Integer  min_price ;//int(4) DEFAULT NULL,
	private Integer  close_price ;//int(4) DEFAULT NULL,
	private Integer  change_amount ;//int(5) DEFAULT NULL,
	private String change_type;
	private Double  change_range ;//double(10,0) DEFAULT NULL COMMENT '涨跌幅度，百分比',
	private Integer   average_price ;//double(10,0) DEFAULT NULL COMMENT '加权平均价',
	private Integer   turnover;// int(11) DEFAULT NULL COMMENT '成交量（公斤）手',
	private Long  turnover_premium;// bigint(20) DEFAULT NULL COMMENT '成交额',
	
	public AgData() {
		super();
	}
	
	
	
	public AgData( String time, Integer open_price, Integer max_price, Integer min_price,
			Integer close_price, Integer change_amount, String change_type, Double change_range, Integer average_price,
			Integer turnover, Long turnover_premium) {
		super();
		this.time = time;
		this.open_price = open_price;
		this.max_price = max_price;
		this.min_price = min_price;
		this.close_price = close_price;
		this.change_amount = change_amount;
		this.change_type = change_type;
		this.change_range = change_range;
		this.average_price = average_price;
		this.turnover = turnover;
		this.turnover_premium = turnover_premium;
	}



	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getOpen_price() {
		return open_price;
	}
	public void setOpen_price(Integer open_price) {
		this.open_price = open_price;
	}
	public Integer getMax_price() {
		return max_price;
	}
	public void setMax_price(Integer max_price) {
		this.max_price = max_price;
	}
	public Integer getMin_price() {
		return min_price;
	}
	public void setMin_price(Integer min_price) {
		this.min_price = min_price;
	}
	public Integer getClose_price() {
		return close_price;
	}
	public void setClose_price(Integer close_price) {
		this.close_price = close_price;
	}
	public Integer getChange_amount() {
		return change_amount;
	}
	public void setChange_amount(Integer change_amount) {
		this.change_amount = change_amount;
	}
	public Double getChange_range() {
		return change_range;
	}
	public void setChange_range(Double change_range) {
		this.change_range = change_range;
	}
	public Integer getAverage_price() {
		return average_price;
	}
	public void setAverage_price(Integer average_price) {
		this.average_price = average_price;
	}
	public Integer getTurnover() {
		return turnover;
	}
	public void setTurnover(Integer turnover) {
		this.turnover = turnover;
	}
	public Long getTurnover_premium() {
		return turnover_premium;
	}
	public void setTurnover_premium(Long turnover_premium) {
		this.turnover_premium = turnover_premium;
	}



	@Override
	public String toString() {
		return "AgData [id=" + id + ", time=" + time + ", open_price=" + open_price + ", max_price=" + max_price
				+ ", min_price=" + min_price + ", close_price=" + close_price + ", change_amount=" + change_amount
				+ ", change_type=" + change_type + ", change_range=" + change_range + ", average_price=" + average_price
				+ ", turnover=" + turnover + ", turnover_premium=" + turnover_premium + "]";
	}
	
}
