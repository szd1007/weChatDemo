package com.jd.star;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UpdateBean updateBean = new UpdateBean();
		updateBean.setOpenidMsgSet(CoreService.openidMsgSet);
		updateBean.setOpenidSet(CoreService.openidSet);
		String reString = JSON.toJSONString(updateBean);
		CoreService.openidMsgSet.clear();
		response.getWriter().print( java.net.URLEncoder.encode(reString, "utf-8"));
	
	}



}
