package org.nuclearzone.alarmclock.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import org.nuclearzone.alarmclock.services.SoundService;
import org.springframework.util.Assert;

/**
 * {@link ActionListener} for stopping a currently playing alarm.
 */
public class AlarmStoppingActionListener implements ActionListener {
	
	private final SoundService soundService;

	public AlarmStoppingActionListener(SoundService soundService) {
		Assert.notNull(soundService, "SoundService cannot be null");
		this.soundService = soundService;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				soundService.stopPlaying();
			}
		});
	}
}