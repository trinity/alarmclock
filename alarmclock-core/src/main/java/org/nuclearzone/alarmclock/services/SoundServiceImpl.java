package org.nuclearzone.alarmclock.services;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.swing.SwingUtilities;

import org.nuclearzone.alarmclock.events.AlarmPlayingStartedEvent;
import org.nuclearzone.alarmclock.events.AlarmPlayingStoppedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Default (simple) {@link SoundService} implementation. This is not thread-safe as we can safely assume (for now) that
 * it's called from EDT only. 
 *
 */
public class SoundServiceImpl implements SoundService, ApplicationEventPublisherAware {
	
	private static final Logger logger = LoggerFactory.getLogger(SoundServiceImpl.class);
	
	private ApplicationEventPublisher publisher;
	
	private Clip clipRef;
	
	@Override
	public void startPlaying(URL url, boolean loop) throws IllegalStateException {			
		if(clipRef != null) {
			throw new IllegalStateException("Sound is already playing");
		}
					
		try {
			final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			AudioFormat format = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			final Clip clip = (Clip)AudioSystem.getLine(info);
	
			clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					if(event.getType() == Type.START) {
						publisher.publishEvent(new AlarmPlayingStartedEvent(SoundServiceImpl.this));
					} else if(event.getType() == Type.STOP) {	
						publisher.publishEvent(new AlarmPlayingStoppedEvent(SoundServiceImpl.this));
						
						// tear the clip down here.
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {

								clip.flush();
								clip.close();
								clipRef = null;
									
								try {
									audioInputStream.close();
								} catch (IOException e) {
									logger.error("Unable to close audio input stream", e);
								}
							}
						});
					}
				}
			});
			
			clipRef = clip;
		
			clip.open(audioInputStream);
			
			if(loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
			
		} catch (Exception e) {
			logger.error("Unable to play sound", e);
			throw new IllegalStateException(e);
		}
	}
			
	@Override
	public boolean isPlaying() {
		return clipRef != null;
	}
	
	@Override
	public void stopPlaying() {
		if(clipRef != null) {
			clipRef.stop();
		}
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}