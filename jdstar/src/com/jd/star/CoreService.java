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
	public static HashMap<String,Integer>userEndflagSet = new HashMap<String,Integer>();//��ͬ���ֵĺ�׺
	public static String registerFlag= "@�Ұ�����";
		
	
    /** 
     * ����΢�ŷ��������� 
     *  
     * @param request 
     * @return 
     */ 
    public static String processRequest(HttpServletRequest request) { 
        String respMessage = null; 
        try { 
            // Ĭ�Ϸ��ص��ı���Ϣ���� 
            String respContent = ""; 
 
            // xml������� 
            Map<String, String> requestMap = MessageUtil.parseXml(request); 
 
            // ���ͷ��ʺţ�open_id�� 
            String fromUserName = requestMap.get("FromUserName"); 
            // �����ʺ� 
            String toUserName = requestMap.get("ToUserName"); 
            // ��Ϣ���� 
            String msgType = requestMap.get("MsgType"); 
 

            // �ı���Ϣ 
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { 
                respContent = ""; 

                
                String reciveContent = requestMap.get("Content");//��ȡ���ν��յ����ı���Ϣ
                writeFile(reciveContent, "click.txt");
                writeFile(requestMap.toString(), "click.txt");
                
                String openid = requestMap.get("FromUserName");
                //�ô���ǩ��ָ��
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
//                	//���û����Ѿ��������û�ռ����
//                	if((!openid.equals(userOpenidSet.get(userName)))){
//                		openidSet.put(openid, userName+""+userEndflagSet.get(userName));//
//                		userEndflagSet.put(userName,userEndflagSet.get(userName)+1);//��׺����1
//                		
//                	}else {
//                    	openidSet.put(openid, userName);//һ��΢���û�ֻ�ܶ�Ӧһ���˻�
//
//					}
                	if(userSet.contains(userName))//���û���ע��
                	{
                    	respContent = generateSendString( 
                    			requestMap.get("FromUserName"),requestMap.get("ToUserName"),
                    			"��Ǹ," +userName+"\r\n"
                    					+ "�Ѿ���ע�ᣬ��ʹ����������");
                	}else {
						userSet.add(userName);
						openidSet.put(openid, userName);//һ��΢���û�ֻ�ܶ�Ӧһ���˻�
	                	respContent = generateSendString( 
	                			requestMap.get("FromUserName"),requestMap.get("ToUserName"),
	                			"��ӭ," +userName+"\r\n"
	                					+ "����ǩ���ɹ������� ���ɻظ���Ϣ����΢��ǽ����Ŷ~");
					}
                	
                	
                	

                		
                	writeFile(respContent, "click.txt");
//                					+ "����ǩ���ɹ������� ���ɻظ���Ϣ����΢��ǽ����Ŷ~");
                	
                
                } else {
                	writeFile("receive message ", "click.txt");
                	if(openidMsgSet.get(openid)==null){
                		ArrayList<String> tmp = new ArrayList<String>();
                		
                		openidMsgSet.put(openid, tmp);
                	}
                	openidMsgSet.get(openid).add(getDate()+"#"+reciveContent);
				}
                
                
            } 
//            // ͼƬ��Ϣ 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { 
//                respContent = "�����͵���ͼƬ��Ϣ��"; 
//            } 
//            // ����λ����Ϣ 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { 
//                respContent = "�����͵��ǵ���λ����Ϣ��"; 
//            } 
//            // ������Ϣ 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { 
//                respContent = "�����͵���������Ϣ��"; 
//            } 
//            // ��Ƶ��Ϣ 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { 
//                respContent = "�����͵�����Ƶ��Ϣ��"; 
//            } 
            // �¼����� 
            //else 
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) { 
                // �¼����� 
                String eventType = requestMap.get("Event"); 
                // ���� 
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { 
                    respContent = "лл���Ĺ�ע��"; 
                } 
                // ȡ������ 
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { 
                    // TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ 
                } 
                // �Զ���˵�����¼� 
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) { 
                    // TODO �Զ���˵�Ȩû�п��ţ��ݲ����������Ϣ 
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
//    	<Content><![CDATA[���]]></Content>
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