package org.nuclearzone.alarmclock.frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.springframework.util.Assert;

/**
 * Main frame
 *
 */
public class AlarmClockFrame extends JFrame {

	private static final long serialVersionUID = 4288976785394696725L;

	public AlarmClockFrame() {
		Assert.isTrue(SwingUtilities.isEventDispatchThread());
		setTitle("Alarm Clock");
		setLayout(new MigLayout("fill, debug"));
		setSize(300, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}