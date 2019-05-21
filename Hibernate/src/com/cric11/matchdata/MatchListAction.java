//$Id$
package com.cric11.matchdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class MatchListAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
	}

	@Override
	public String execute() throws Exception {
		String httpMethod = request.getMethod();
		switch (httpMethod) {
		case "get":
			
			break;

		default:
			break;
		}
		return null;
	}
	
	
	

}
