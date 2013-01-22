package org.nuclearzone.alarmclock.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.nuclearzone.alarmclock.panel.MainPanel;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.Assert;

/**
 * {@link ActionListener} for updating clock time on {@link MainPanel}
 */
public class ClockActionListener implements ActionListener {
	
	private final JTextPane textField;

	public ClockActionListener(JTextPane textField) {
		Assert.notNull(textField, "JTextField for clock time cannot be null");
		this.textField = textField;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				DateFormatter formatter = new DateFormatter("HH:mm:ss");
		        textField.setText(formatter.print(new Date(), Locale.getDefault()));
			}
			
		});
	}
}