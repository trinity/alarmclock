package org.nuclearzone.alarmclock.events;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

/**
 * Event fired when alarm playing is started, used to enable the 'stop alarm' button in the UI
 */
public class AlarmPlayingStartedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public Date getDate() {
		return new Date(getTimestamp());
	}
	
	public AlarmPlayingStartedEvent(Object source) {
		super(source);
	}
}
