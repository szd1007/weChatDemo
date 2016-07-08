package com.jd.star;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;


public class CoreService { 
		
	public static HashSet<String>clickSet = new HashSet<String>();
	public static List<String> mesList= new ArrayList<String>();
	public static HashSet<String>userSet = new HashSet<String>();
	public static HashMap<String,String>openidSet = new HashMap<String,String>();
	public static HashMap<String,String>userOpenidSet = new HashMap<String,String>();
	public static ConcurrentHashMap<String, ArrayList<String>> openidMsgSet = new ConcurrentHashMap<String,ArrayList<String>>();
	public static HashMap<String,Integer>userEndflagSet = new HashMap<String,Integer>();//相同名字的后缀
	public static String registerFlag= "@我爱京东";
		
	
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */ 
    public static String processRequest(HttpServletRequest request) { 
        String respMessage = null; 
        try { 
            // 默认返回的文本消息内容 
            String respContent = ""; 
 
            // xml请求解析 
            Map<String, String> requestMap = MessageUtil.parseXml(request); 
 
            // 发送方帐号（open_id） 
            String fromUserName = requestMap.get("FromUserName"); 
            // 公众帐号 
            String toUserName = requestMap.get("ToUserName"); 
            // 消息类型 
            String msgType = requestMap.get("MsgType"); 
 

            // 文本消息 
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { 
                respContent = ""; 

                
                String reciveContent = requestMap.get("Content");//获取本次接收到的文本信息
                writeFile(reciveContent, "click.txt");
                writeFile(requestMap.toString(), "click.txt");
                
                String openid = requestMap.get("FromUserName");
                //该次是签到指令
                if(reciveContent.endsWith(registerFlag)){
                	writeFile("register  success", "click.txt");
                	
                	String userName = reciveContent.substring(0, reciveContent.indexOf(registerFlag));
//                	if(userOpenidSet.get(userName)==null){
//                		userOpenidSet.put(userName, openid);
//                	}
//                	if(userEndflagSet.get(userName)==null){
//                	   userEndflagSet.put(userName, 1);
//
//                	}
//                	//该用户名已经被其他用户占用了
//                	if((!openid.equals(userOpenidSet.get(userName)))){
//                		openidSet.put(openid, userName+""+userEndflagSet.get(userName));//
//                		userEndflagSet.put(userName,userEndflagSet.get(userName)+1);//后缀名加1
//                		
//                	}else {
//                    	openidSet.put(openid, userName);//一个微信用户只能对应一个账户
//
//					}
                	if(userSet.contains(userName))//该用户已注册
                	{
                    	respContent = generateSendString( 
                    			requestMap.get("FromUserName"),requestMap.get("ToUserName"),
                    			"抱歉," +userName+"\r\n"
                    					+ "已经被注册，请使用其他名称");
                	}else {
						userSet.add(userName);
						openidSet.put(openid, userName);//一个微信用户只能对应一个账户
	                	respContent = generateSendString( 
	                			requestMap.get("FromUserName"),requestMap.get("ToUserName"),
	                			"欢迎," +userName+"\r\n"
	                					+ "您已签到成功！！！ 您可回复消息参与微信墙互动哦~");
					}
                	
                	
                	

                		
                	writeFile(respContent, "click.txt");
//                					+ "您已签到成功！！！ 您可回复消息参与微信墙互动哦~");
                	
                
                } else {
                	writeFile("receive message ", "click.txt");
                	if(openidMsgSet.get(openid)==null){
                		ArrayList<String> tmp = new ArrayList<String>();
                		
                		openidMsgSet.put(openid, tmp);
                	}
                	openidMsgSet.get(openid).add(getDate()+"#"+reciveContent);
				}
                
                
            } 
//            // 图片消息 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { 
//                respContent = "您发送的是图片消息！"; 
//            } 
//            // 地理位置消息 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { 
//                respContent = "您发送的是地理位置消息！"; 
//            } 
//            // 链接消息 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { 
//                respContent = "您发送的是链接消息！"; 
//            } 
//            // 音频消息 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { 
//                respContent = "您发送的是音频消息！"; 
//            } 
            // 事件推送 
            //else 
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) { 
                // 事件类型 
                String eventType = requestMap.get("Event"); 
                // 订阅 
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { 
                    respContent = "谢谢您的关注！"; 
                } 
                // 取消订阅 
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { 
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息 
                } 
                // 自定义菜单点击事件 
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) { 
                    // TODO 自定义菜单权没有开放，暂不处理该类消息 
                	String menu = requestMap.get("EventKey");
                	if(!clickSet.contains(menu)){
                		clickSet.add(menu);
                		writeFile(menu, "click.txt");
                	}
                } 
            } 
            writeFile("@@", "click.txt");

            respMessage = respContent;
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
        return respMessage; 
    } 
    
    public  static String getDate(){
        return getDate("HH:mm:ss");
    }
    public static String getDate(String format){
        return new SimpleDateFormat(format).format(new Date());
    }

  
    public static void writeFile(String content ,String fileName) {  
        try {  
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(  
                    new FileOutputStream(fileName,true),  
                    "UTF-8"));  
            out.write(content+"\r\n");  
            out.newLine();  
            out.flush();  
            out.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static String generateSendString(String toUser,String fromUser,String content){
//    	<xml>
//    	<ToUserName><![CDATA[toUser]]></ToUserName>
//    	<FromUserName><![CDATA[fromUser]]></FromUserName>
//    	<CreateTime>12345678</CreateTime>
//    	<MsgType><![CDATA[text]]></MsgType>
//    	<Content><![CDATA[你好]]></Content>
//    	</xml>

    	String reString ="<xml>"
    			+ "<ToUserName><![CDATA["+toUser+"]]></ToUserName>"
    			+ "<FromUserName><![CDATA["+fromUser+"]]></FromUserName>"
    			+ "<CreateTime>12345678</CreateTime>"
    			+ "<MsgType><![CDATA[text]]></MsgType>"
    			+ "<Content><![CDATA["+content+"]]></Content>"
    			+"</xml>";
    	return reString;
    	
    }
} 