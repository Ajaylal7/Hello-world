package com.cric11.utility;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.cric11.matchdata.MatchListScheduler;
import com.opensymphony.xwork2.ActionSupport;

public class MatchSquad extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	
	private static Logger LOGGER = Logger.getLogger(MatchSquad.class.getName());
	
	private HttpServletResponse response;
	
	private HttpServletRequest request; 

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
	}

	public String execute() {
		String espn_url = "http://www.espncricinfo.com/{country}/content/player/{pid}.html";
		String player_role_url = "http://search.espncricinfo.com/ci/content/player/search.html?search=";
		String match_url = "http://cricapi.com/api/matches";
		String squad_url = "http://cricapi.com/api/fantasySquad";
		String cricbuzzUrl = "https://www.cricbuzz.com/match-api/22526/commentary.json";
		String mytean11url = "https://web.myteam11.com/match/getmatchlist";
		System.out.println("Hello there....");
		try {
		
		HashMap<String, String> params = new HashMap<>();
		params.put("apikey", "r3vpuMv5Yke7eAxLwPXzWgePegQ2");
		String matchData = Cric11Utility.getResponsefromUrl(match_url, "post", null, params, null);
		String playing11 = null;
		HashMap<String, String> myteam11Header = new HashMap<>();
		myteam11Header.put("appCode", "1");
		myteam11Header.put("ExpireToken","3D1FFD18-9A58-4E96-9DF4-C5C77FD5D950");
		myteam11Header.put("matchType", "1");
		myteam11Header.put("AuthExpire", "a01e6c10-e91d-4a00-8328-24e471620f10");
		myteam11Header.put("UserId","727344");
		String team11Matches = Cric11Utility.getResponsefromUrl(mytean11url, "get", myteam11Header, null, null);
		JSONObject matchschedule = new JSONObject(playing11);
		
		//JSONArray scheduleArray = matchschedule.getJSONArray("matches");
		
		Configuration configuration = new Configuration().configure().addAnnotatedClass(Players.class);
		SessionFactory session = configuration.buildSessionFactory();
		
		Session ses = session.openSession();
		
		Transaction transaction = ses.beginTransaction();

		//ses.save(player);
		
		transaction.commit();
		
		ses.close();
		
		PrintWriter writer = response.getWriter();
		writer.write(matchschedule.toString());
		writer.flush();
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
		return null;
	}
	
	public String stringCheck() {
		String playerData[] = new String[2];
		try {
			JobDetail job = JobBuilder.newJob(MatchListScheduler.class).withIdentity("matchListJob", "group1").build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("matchListTrigger", "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
			
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
		}
		catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception in data fetching:"+ex);
		}
		return null;
	}
	
	public String timerCheck() {
		try {
			Timer timer = new Timer();
			timer.schedule(new JavaTimer(), new Date((new Date().getTime()+5000)));
			
		}
		catch(Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception in creating timer data:{0}",ex);
		}
		return null;
	}
	
}
