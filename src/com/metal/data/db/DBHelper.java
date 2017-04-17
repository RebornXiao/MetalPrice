package com.metal.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
	/*
	 * String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; String
	 * url = "jdbc:sqlserver://127.0.0.1;DatabaseName=javenforexcel";
	 */

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/my_metal_data";

	Connection con = null;
	ResultSet res = null;

	public void DataBase() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, "root", "root");
		} catch (ClassNotFoundException e) {
			System.err.println("װ�� JDBC/ODBC ��������ʧ�ܡ�");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("�޷��������ݿ�");
			e.printStackTrace();
		} 
//		finally {
//			if (con != null) {
//				try {
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	// ��ѯ
	public ResultSet Search(String sql, String str[]) {
		DataBase();
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					pst.setString(i + 1, str[i]);
				}
			}
			res = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// ��ɾ�޸�
	public int AddU(String sql, String str[]) {
		int a = 0;
		DataBase();
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					pst.setString(i + 1, str[i]);
				}
			}
			a = pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}
}
