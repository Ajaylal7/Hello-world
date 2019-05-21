//$Id$
package com.cric11.matchdata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cric11.utility.Cric11Utility;
import com.cric11.utility.HibernateUtil;

public class MatchListScheduler implements Job {
	
	private static Logger LOGGER = Logger.getLogger(MatchListScheduler.class.getName());
	
	String matchListUrl = "http://cricapi.com/api/matches";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
		HashMap<String, String> params = new HashMap<>(),a;
		params.put("apikey", "r3vpuMv5Yke7eAxLwPXzWgePegQ2");
		
		String team11Matches = Cric11Utility.getResponsefromUrl(matchListUrl, "post", null, params, null);
		
		JSONObject matchJSON = new JSONObject(team11Matches);
		
		if(matchJSON.has("matches")) {
			JSONArray matchArray = matchJSON.getJSONArray("matches");
			for(int index = 0; index<matchArray.length(); index++) {
				JSONObject matchData = matchArray.getJSONObject(index);
				if(!Boolean.valueOf(matchData.get("matchStarted").toString())) {
					MatchListData matchObj = new MatchListData();
					matchObj.setMatchStatus(MatchListConstants.MatchStatus.FIXTURES.getStatusId());
					String team1 = matchData.get("team-1").toString();
					String team2 = matchData.get("team-2").toString();
					matchObj.setTeamName1(team1);
					matchObj.setTeamName2(team2);
					String startTimeGMT = matchData.get("dateTimeGMT").toString();
					
//					ZonedDateTime date = ZonedDateTime.
//					String startDate = date.format(DateTimeFormatter.ISO_DATE_TIME);
					Instant instant = Instant.parse(startTimeGMT).with(ChronoField.NANO_OF_SECOND,0);
					DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
					Date date1 = iso8601.parse(instant.toString());
					Long time1 = date1.getTime();
					
					matchObj.setStartTime(time1);
					String relatedName = team1+" vs "+team2+" "+matchData.get("type").toString();
					matchObj.setRelatedName(relatedName);
					Session session = HibernateUtil.session.openSession();
					
					Transaction transaction = session.beginTransaction();
					
					session.save(matchObj);
					
					transaction.commit();
					
					session.close();
					
					
					
				}
			}
		}
		
		}
		catch(Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception in fetching data:",ex);
		}
	}

}
