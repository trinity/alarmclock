package org.nuclearzone.alarmclock.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.nuclearzone.alarmclock.panel.MainPanel;
import org.nuclearzone.alarmclock.services.SoundService;
import org.springframework.util.Assert;

/**
 * {@link ActionListener} for firing alarms
 */
public class AlarmActionListener implements ActionListener {
	
	private Date fireDate;
	private boolean loop;
	
	private boolean alreadyFired = false;
	
	private final MainPanel mainPanel;
	private final SoundService soundService;
	
	public AlarmActionListener(MainPanel mainPanel, SoundService soundService) {
		Assert.notNull(mainPanel, "Main panel cannot be null");
		Assert.notNull(soundService, "SoundService cannot be null");
		this.mainPanel = mainPanel;
		this.soundService = soundService;
	}
	
	public void setFireDate(Date fireDate) {
		Assert.notNull(fireDate, "Fire date cannot be null");
		this.fireDate = fireDate;
	}

	public void setLoop(boolean value) {
		this.loop = value;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Assert.notNull(fireDate, "Firing date for alarm cannot be null");
        
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Calendar now = Calendar.getInstance();
		        Calendar fire = Calendar.getInstance();
		        fire.setTime(fireDate);
		        if(now.get(Calendar.HOUR_OF_DAY) == fire.get(Calendar.HOUR_OF_DAY) &&
		        		now.get(Calendar.MINUTE) == fire.get(Calendar.MINUTE)) {
		        	if(!alreadyFired) {
		        		try {
		        			if(!soundService.isPlaying()) { 
				        		soundService.startPlaying(this.getClass().getResource("/alarm.wav"), loop);
				        	}
		        		} catch(final IllegalStateException ex) {
		        			JOptionPane.showMessageDialog(mainPanel, "Unable to play sound: " + ex.getCause().getMessage(), "Unable to play sound", JOptionPane.ERROR_MESSAGE);
						} finally {
		        			alreadyFired = true;
		        		}
		        	}
		        } else {
		        	alreadyFired = false;
		        }
			}
		});
	}
}