package org.nuclearzone.alarmclock.panel;

import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;

import org.nuclearzone.alarmclock.events.AlarmPlayingStartedEvent;
import org.nuclearzone.alarmclock.events.AlarmPlayingStoppedEvent;
import org.nuclearzone.alarmclock.listeners.AlarmActionListener;
import org.nuclearzone.alarmclock.listeners.AlarmSettingActionListener;
import org.nuclearzone.alarmclock.listeners.AlarmStoppingActionListener;
import org.nuclearzone.alarmclock.listeners.ClockActionListener;
import org.nuclearzone.alarmclock.services.SoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.format.datetime.DateFormatter;

/**
 * Main panel
 */
public class MainPanel extends JPanel implements ApplicationListener<ApplicationEvent>, InitializingBean {
	
	private static final long serialVersionUID = -7095561956962872545L;

	private static final Logger logger = LoggerFactory.getLogger(MainPanel.class);
	
	@Autowired
	private SoundService soundService;
	
	private JButton stopAlarm;
	
	/**
	 * Build the layout here.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
			
		setLayout(new MigLayout("fill"));

		JTextPane currentTimeField = new JTextPane();
	    MutableAttributeSet attrs = currentTimeField.getInputAttributes();
	    StyleConstants.setFontSize(attrs, 40);
	    StyledDocument doc = currentTimeField.getStyledDocument();
	    doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
	    currentTimeField.setEditable(false);
		currentTimeField.setBackground(getBackground());
		
		DateFormatter formatter = new DateFormatter("HH:mm:ss");
        currentTimeField.setText(formatter.print(new Date(), Locale.getDefault()));
		add(currentTimeField, "align center, wrap");
		
		final Timer timer = new Timer(1000, new ClockActionListener(currentTimeField));
	    timer.start();
	    
	    JPanel timeContainer = new JPanel(new MigLayout("nogrid"));
	    
	    JTextPane text = new JTextPane();
	    text.setText("Alarm time:");
	    text.setBackground(getBackground());
	    
	    timeContainer.add(text);
	    
	    final JTextField newAlarmTimeField = new JTextField(4);
	    timeContainer.add(newAlarmTimeField);
	    add(timeContainer, "align center, wrap");
	    
	    JPanel loopContainer = new JPanel(new MigLayout("nogrid"));
	    add(loopContainer, "align center, wrap");
	    
	    final JCheckBox loopSelection = new JCheckBox();
	    loopContainer.add(loopSelection);
	    
	    JTextPane loopText = new JTextPane();
	    loopText.setText("Play alarm indefinitely");
	    loopText.setBackground(getBackground());
	    loopContainer.add(loopText);
	   	    
	    AlarmActionListener alarmListener = new AlarmActionListener(this, soundService);
	    
	    final JButton setButton = new JButton("Set alarm");
	    setButton.addActionListener(new AlarmSettingActionListener(this, alarmListener, setButton, loopSelection,
				newAlarmTimeField, timer));
	    add(setButton, "align center, span 2");
	    
	    stopAlarm = new JButton("Stop alarm");
	    stopAlarm.addActionListener(new AlarmStoppingActionListener(soundService));
	}
	
	@Override
	public void onApplicationEvent(final ApplicationEvent event) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if(event instanceof AlarmPlayingStartedEvent) {
					logger.info("Alarm playing started on {}", ((AlarmPlayingStartedEvent) event).getDate()); 
					add(stopAlarm, "newline, align center, span 2");
				} else if(event instanceof AlarmPlayingStoppedEvent) {
					logger.info("Alarm playing stopped on {}", ((AlarmPlayingStoppedEvent) event).getDate());
					remove(stopAlarm);
					repaint();
				}
			}
		});
	}
}