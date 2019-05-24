//$Id$
package com.cric11.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.cric11.matchdata.MatchListScheduler;

public class HibernateUtil implements ServletContextListener {
	
	private static Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());
	
	public static Configuration configuration = null;
	public static SessionFactory session = null;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		session.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		 configuration = new Configuration().configure();
		session = configuration.buildSessionFactory();
		
		//HibernateUtil.startMatchDataFetchScheduler();
		
	}

	private static void startMatchDataFetchScheduler() {
		try {
			JobDetail job = JobBuilder.newJob(MatchListScheduler.class).withIdentity("matchListJob", "group1").build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("matchListTrigger", "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever()).build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		}
		catch(Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception in starting matchdata fetch scheduler:"+ex);
		}
	}

}
