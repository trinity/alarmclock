package org.nuclearzone.alarmclock.main;

import javax.swing.SwingUtilities;

import org.nuclearzone.alarmclock.frame.AlarmClockFrame;
import org.nuclearzone.alarmclock.panel.MainPanel;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class of this super fine ye' olde alarm clock app
 * 
 * TODO: add Spotify support via JNA maybe & rewrite in JavaFX 2
 */
public class AlarmClock {

	public static void main(String[] args) {	
		
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:alarmClockContext.xml");
		context.registerShutdownHook();
		
		// make sure the main frame really is in EDT to avoid any problems
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				AlarmClockFrame frame = new AlarmClockFrame();
				MainPanel mainPanel = (MainPanel) context.getBean("mainPanel");
				
				frame.setContentPane(mainPanel);
				frame.setVisible(true);
			}
		});
	}
}