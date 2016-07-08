package com.jd.si;

import java.sql.ResultSet;  
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import util.UpdateBean;  
  
public class InserDbData {  
  
    static String sql = null;  
    static DBHelper db1 = new DBHelper(sql);//创建DBHelper对象  
    static ResultSet ret = null;  
  
    public static void insertUser(UpdateBean updateBean){
    	HashMap<String,String>openidSet = updateBean.getOpenidSet();
    	for(Map.Entry<String, String>entry :openidSet.entrySet()){
    		String openId = entry.getKey();
    		String userName = entry.getValue();
    		String tmpSql = "select * from register where "
    				+ "wechatname ='"+userName+"'";    		
    		if(isNullRecord(tmpSql)){
    			String tmpDeleteSql = "delete  from register where openid='"+openId+"'";
    			System.out.println(tmpDeleteSql);
    			db1.executeSql(tmpDeleteSql);
    			
    			String tmpInsertSql = "insert into register (wechatname,openid) values ('"+userName+"',"
    					+ "'"+openId+"')";
    			System.out.println(tmpInsertSql);
    			db1.executeSql(tmpInsertSql);
    			//System.out.println("add user ["+userName+"]to table register");
    		}


    	}
    	
    }
    public static void insertMsg(UpdateBean updateBean){
    	ConcurrentHashMap<String, ArrayList<String>>openidMsgSet = updateBean.getOpenidMsgSet();
    	for(Map.Entry<String, ArrayList<String>>entry :openidMsgSet.entrySet()){
    		String openId = entry.getKey();
    		String userName = updateBean.getOpenidSet().get(openId);
    		if(userName==null){
    			continue;
    		}
    		for(String msg:entry.getValue()){
    			String[] tmp = msg.split("#");
    			
    			String tmpInsertSql = "insert into wechatwall ("
    					+ "open_id,user_name,message,timestamp) values (0,'"+userName+"'"
    					+ ",'"+tmp[1]+"','"+tmp[0]+"')";
    			System.out.println(tmpInsertSql);
    			db1.executeSql(tmpInsertSql);
    			System.out.println("add MSG: user["+userName+"]  message["+tmp[1]+"]");
    		}
    	}
    	
    }
    private static boolean isNullRecord(String sql){
    	ResultSet rSet = db1.executeQuerySql(sql);
    	try {
			while(rSet.next()){
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return true;
    }
    public static void main(String[] args) {  
        //sql = "select * from wechatwall";//SQL语句  
        sql = "select * from wechatwall";//SQL语句  
        try {  
        	System.out.println(isNullRecord(sql));
        	
            ret = db1.executeQuerySql(sql);
            
            while (ret.next()) {  
                String uid = ret.getString(1);  
                String ufname = ret.getString(2);  
                String ulname = ret.getString(3); 
                String ms = ret.getString(4); 
                String time = String.valueOf(ret.getTime(5));
                System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t"+ms  +"\t"+time);  
                
              //  System.out.println(uid + "\t" + ufname + "\t" + ulname );
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
}  