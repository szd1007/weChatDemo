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
	// �Զ��� token
	private String TOKEN = "wap_682186";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ΢�ż���ǩ��
		String signature = request.getParameter("signature");
		// ����ַ���
		String echostr = request.getParameter("echostr");
		// ʱ���
		String timestamp = request.getParameter("timestamp");
		// �����
		String nonce = request.getParameter("nonce");

		String[] str = { TOKEN, timestamp, nonce };
		Arrays.sort(str); // �ֵ�������
		String bigStr = str[0] + str[1] + str[2];
		// SHA1����
		String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

		// ȷ����������΢��
		if (digest.equals(signature)) {
			response.getWriter().print(echostr);
		}
	}


    /** 
     * ����΢�ŷ�������������Ϣ 
     */ 
    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        // ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩 
        request.setCharacterEncoding("UTF-8"); 
        response.setCharacterEncoding("UTF-8"); 
 
        // ���ú���ҵ���������Ϣ��������Ϣ 
        String respMessage = CoreService.processRequest(request); 
    
        PrintWriter out = response.getWriter(); 
        out.print(respMessage); 
        out.close(); 
    } 
}
