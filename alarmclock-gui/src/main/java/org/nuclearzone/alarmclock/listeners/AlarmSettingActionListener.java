package org.nuclearzone.alarmclock.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.nuclearzone.alarmclock.panel.MainPanel;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link ActionListener} for setting new alarms. Bound to 'set alarm' button in {@link MainPanel}.
 */
public class AlarmSettingActionListener implements ActionListener {
	
	private final AlarmActionListener alarm;
	private final JButton setButton;
	private final JCheckBox loopSelection;
	private final JTextField newAlarmTimeField;
	private final Timer timer;
	private final MainPanel mainPanel;

	public AlarmSettingActionListener(MainPanel mainPanel, AlarmActionListener alarmListener, JButton setButton,
			JCheckBox loopSelection, JTextField alarmTimeField, Timer timer) {
		Assert.notNull(mainPanel, "MainPanel cannot be null");
		Assert.notNull(alarmListener, "AlarmActionListener cannot be null");
		Assert.notNull(setButton, "Alarm setting button cannot be null");
		Assert.notNull(loopSelection, "Loop selection checkbox cannot be null");
		Assert.notNull(alarmTimeField, "Alarm time field cannot be null");
		Assert.notNull(timer, "Timer cannot be null");
		this.mainPanel = mainPanel;
		this.alarm = alarmListener;
		this.setButton = setButton;
		this.loopSelection = loopSelection;
		this.newAlarmTimeField = alarmTimeField;
		this.timer = timer;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if(newAlarmTimeField.isEditable()) {
					if(!StringUtils.hasText(newAlarmTimeField.getText())) {
						JOptionPane.showMessageDialog(mainPanel, "Alarm time cannot be empty! Format is 'MM:SS'", "Alarm time cannot be empty", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					DateFormatter formatter = new DateFormatter("HH:mm");
					Date parsed = null;
					try {
						parsed = formatter.parse(newAlarmTimeField.getText(), Locale.getDefault());
					} catch (ParseException e) {
						JOptionPane.showMessageDialog(mainPanel, "Invalid alarm time! Format is 'MM:SS'", "Invalid alarm time", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					newAlarmTimeField.setEditable(false);
					loopSelection.setEnabled(false);
					setButton.setText("Remove alarm");
					
					timer.removeActionListener(alarm);
					alarm.setFireDate(parsed);
					alarm.setLoop(loopSelection.isSelected());
					timer.addActionListener(alarm);
				} else {
					newAlarmTimeField.setEditable(true);
					loopSelection.setEnabled(true);
					setButton.setText("Set alarm");
					timer.removeActionListener(alarm);
				}
			}
			
		});
	}
}