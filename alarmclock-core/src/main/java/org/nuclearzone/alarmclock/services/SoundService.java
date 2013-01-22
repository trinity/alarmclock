package org.nuclearzone.alarmclock.services;

import java.net.URL;

/**
 * Simple service for playing sounds
 */
public interface SoundService {

	/**
	 * Play a sound
	 * @param url to load sound file from
	 * @param loop should the sound play indefinitely
	 * @throws IllegalStateException if there is a problem playing the sound
	 */
	void startPlaying(URL url, boolean loop) throws IllegalStateException;
	
	/**
	 * Is a sound currently playing
	 * @return value
	 */
	boolean isPlaying();
	
	/**
	 * Stop playing
	 */
	void stopPlaying();
}