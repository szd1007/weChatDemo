package com.jd.si;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.alibaba.fastjson.JSON;

import util.UpdateBean;


/**
 * Created by shangzhidong on 2016/4/26.
 */
public class DivinerHttpSimulationJDSTAR {

    private static String requestUrl="http://www.uv-w.com/jdstar/update";
    //private static HttpClient client =new DefaultHttpClient();
  //  private static final Log logger = LogFactory.getLog(DivinerHttpSimulationJDSTAR.class);
    private static  int flag=1;
    private static List<Thread> threads=null;
    public static void main(String args[])throws Exception{

        //parseParam(args);
        while (true){
            runLoop();
            TimeUnit.SECONDS.sleep(5);

        }

    }



    /**
     * 每个进程按照指定的循环次数运行一   */
    private static void runLoop(){
         HttpClient client = new DefaultHttpClient();



            Long start = System.currentTimeMillis();
            Long end =null;
            String response = null;
            try {
                response = getDivinerResponse( client);//获取json             
                UpdateBean result = JSON.parseObject(response, UpdateBean.class);//将获取的接口数据转
                
                //将用户名插入用户表
                InserDbData.insertUser(result);
                //将获取的聊天信息插入wechatwall表
                InserDbData.insertMsg(result);
                
                System.out.println(response);
                end = System.currentTimeMillis();

            } catch (Exception e) {
                e.printStackTrace();
                //logger.error(e.getMessage());
            }




    }
    public static String getDivinerResponse(HttpClient client)throws Exception{
        HttpGet request = new HttpGet(requestUrl);

//        if(!"".equals(SystemConfig.cookies)){
//            //权限cookie
//            request.addHeader("Cookie", SystemConfig.cookies);
//        }
//        request.addHeader("Connection","");
        
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        StringBuffer sb = new StringBuffer();

        Header[]resHeader = response.getAllHeaders();
//        for(Header header:resHeader){
//            sb.append(header.getName());
//            sb.append("$$");
//            sb.append(header.getValue());
//            sb.append("\r\n");
//        }
        while((line = rd.readLine()) != null) {
            sb.append(line);
        }
        
        return java.net.URLDecoder.decode(sb.toString(), "utf-8");
    }

    private static String getPin(String url){
        if(!url.contains("pin=")){
            return "";
        }
        int startLoc = url.indexOf("pin=")+4;
        int stopLoc = url.indexOf('&',url.indexOf("pin="));
        if(stopLoc<0)
            stopLoc = url.length();
        return url.substring(startLoc,stopLoc);
    }

 
}
