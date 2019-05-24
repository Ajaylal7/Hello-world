//$Id$
package com.cric11.user;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class VerifyUser extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	@Override
	public String execute() throws Exception {
		
		try {
		
		final String userName = "ajay7hariharan@gmail.com";
        final String password = "chesslotuspetal";
		
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
        
        Session session = Session.getInstance(props, new Authenticator() {
        	@Override
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(userName, password);
        	}
		});
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ajay7hariharan@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("ajaylal.sh@zohocorp.com")
        );
        message.setSubject("Testing Gmail TLS");
        message.setText("Home page paningala??????");

        Transport.send(message);
        

        System.out.println("Done");
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}

		return null;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

}
