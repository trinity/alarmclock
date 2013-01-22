package org.nuclearzone.alarmclock.events;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

/**
 * Event fired when alarm playing is stopped, used to disable the 'stop alarm' button in the UI
 */
public class AlarmPlayingStoppedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public Date getDate() {
		return new Date(getTimestamp());
	}
	
	public AlarmPlayingStoppedEvent(Object source) {
		super(source);
	}
}
