package com.jd.star;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 自定义 token
	private String TOKEN = "wap_682186";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");

		String[] str = { TOKEN, timestamp, nonce };
		Arrays.sort(str); // 字典序排序
		String bigStr = str[0] + str[1] + str[2];
		// SHA1加密
		String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

		// 确认请求来至微信
		if (digest.equals(signature)) {
			response.getWriter().print(echostr);
		}
	}


    /** 
     * 处理微信服务器发来的消息 
     */ 
    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码） 
        request.setCharacterEncoding("UTF-8"); 
        response.setCharacterEncoding("UTF-8"); 
 
        // 调用核心业务类接收消息、处理消息 
        String respMessage = CoreService.processRequest(request); 
    
        PrintWriter out = response.getWriter(); 
        out.print(respMessage); 
        out.close(); 
    } 
}
