package com.jd.si;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  
  
public class DBHelper {  
    public static final String url = "jdbc:mysql://127.0.0.1/jing5";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "s6880748";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public DBHelper(String sql) {  
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
           
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    public ResultSet executeQuerySql(String sql){
    	 try {
			pst = conn.prepareStatement(sql);
			return pst.executeQuery();//执行语句，得到结果集  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//准备执行语句  
		return null;
    }
    public boolean executeSql(String sql){
   	 try {
			return pst.execute(sql);//执行语句，得到结果集  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//准备执行语句  
		return false;
   } 
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
} 