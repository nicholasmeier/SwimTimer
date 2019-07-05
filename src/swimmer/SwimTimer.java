package swimmer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;	
import javax.swing.JLabel;
import javax.swing.JTextField;



public class SwimTimer{
	/* Output Values
	*  # of laps
	*  avg time of each lap
	*  total time of session  
	*/ 

	int count_laps = 0;
	double avg_lap_time = 0;
	double total_time = 0;
	Coach c = new Coach();
	
	public SwimTimer () {
		
		JFrame frame = new JFrame("Swim Timer");
		JLabel label_Laps = new JLabel();
		JLabel label_Avg = new JLabel();
		JLabel label_Total = new JLabel();
		label_Laps.setText("Number of Laps: _");
		label_Avg.setText("Average Lap Time: _ seconds");
		label_Total.setText("Total Time: _ seconds");
		
		label_Laps.setBounds(50, 150, 200, 40);
		label_Avg.setBounds(50, 170, 200, 40);
		label_Total.setBounds(50, 190, 200, 40);
		//label_Laps.setVisible(false);
		
		JButton button_Start = new JButton("Start Timer");
		button_Start.setBounds(75,50,140,40);
		JButton button_Stop = new JButton("Stop Timer");
		button_Stop.setBounds(75,100,140,40);
		button_Stop.setEnabled(false);
		
		button_Start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Reset output values
				label_Laps.setText("Number of Laps: _");
				label_Avg.setText("Average Lap Time: _ seconds");
				label_Total.setText("Total Time: _ seconds");
				count_laps = 0;
				avg_lap_time = 0;
				total_time = 0;
				
				
				//Run coach timer
				c.startTiming();
				
				//disable start btn, enable stop btn
				button_Start.setEnabled(false);
				button_Stop.setEnabled(true);
			}
		});
		
		button_Stop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				label_Laps.setVisible(true);
				
				//Stop the coach's timing
				c.stopTiming();
				
				//retrieve coach's info, calc avg lap time
				int[] i = c.showStats();
				count_laps = i[0];
				total_time = i[1];
				avg_lap_time = total_time/((double)count_laps);
				
				label_Laps.setText("Number of Laps: " + count_laps);
				label_Avg.setText("Average Lap Time: " + (avg_lap_time/1000) + " seconds");
				label_Total.setText("Total Time: " + (total_time/1000) + " seconds");
				
				//disable stop btn, enable start btn
				button_Start.setEnabled(true);
				button_Stop.setEnabled(false);
			}
		});
		frame.add(button_Start);
		frame.add(button_Stop);
		frame.add(label_Laps);
		frame.add(label_Total);
		frame.add(label_Avg);
		frame.setSize(300, 300);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public static void main(String args[]) {
		new SwimTimer();
	}
}

//helper class to deal with the timing
class Coach implements ActionListener{
	private int min = 15000;
	private int max = 30000;
	Random randomTime;
	int randomTimeOutput;
	Timer timerRunAction;
	
	private int laps = 0;
	private int timetotal = 0;
	
	public Coach() {
		randomTime = new Random();
		randomTimeOutput = (min + randomTime.nextInt(max - min + 1));
		timetotal += randomTimeOutput;
		//System.out.println(randomTimeOutput);
		timerRunAction = new Timer(randomTimeOutput, this);
	}
	
	public int[] showStats() {
		int[] i = {laps, timetotal};
		return i;
	}
	
	public void startTiming() {
		timerRunAction.start();
		laps = 0;
		timetotal = 0;
	}
	
	public void stopTiming() {
		timerRunAction.stop();
	}
	
	private void updateTimer() {
		timerRunAction.stop();
		randomTimeOutput =  (min + randomTime.nextInt(max - min + 1));
		timetotal += randomTimeOutput;
		//System.out.println(randomTimeOutput);
		timerRunAction.setDelay(randomTimeOutput);
		timerRunAction.restart();
	}
	
	private void playsound() {
		try {
			File file = new File(System.getProperty("user.dir") + "\\beep.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		playsound();
		laps++;
		updateTimer();
	}
}

