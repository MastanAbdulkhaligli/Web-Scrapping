package ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

import java.text.SimpleDateFormat;

public class Panel extends JPanel implements ActionListener{
	
	/**
	 * Variable declaration
	 */
	// Panels
	private JPanel panelSelectedClue;	// Panel used to show the selected clue
	private JPanel panelMatrix;		// Panel used to print grids onto
	
	// Labels
	private JLabel labelAccross;	// Prints the word "ACROSS"
	private JLabel labelDown;	// Prints the word "DOWN"
	private JLabel labelSelectedClue;	// Label of the panelSelectedClue
	private JLabel dateTimeGroupName;	// Holds current date and time
	private JLabel labelElapsedTime;	// Holds elapsed time that user used to complete the puzzle
	
	// Buttons
	private JButton buttonSelectedClue;	// Button that holds the latest selected clue button
	
	// Date and Time
	private SimpleDateFormat dateFormat;	// Date and time formatter
	private JButton buttonRevealPuzzle;		// Reveal puzzle button
	
	// Music
	private AudioInputStream audioInputStream;	// Variable used to get the location of the input stream for the audio (original music/sound)
	private Clip clip;	// Variable used to play the input audio (original music/sound) 
	
	// File Reading
	private File myFile;
	private Scanner myScanner;
	
	// Lists
	private ArrayList<Box> grids;	// List that holds all the boxes on the grid
	private ArrayList<JButton> buttonAcrossList;	// List that holds all across clues' buttons
	private ArrayList<JButton> buttonDownList;		// List that holds all down clues' buttons
	private ArrayList<String> acrossCluesList;		// List that holds all across clues' text and numbers
	private ArrayList<String> downCluesList;		// List that holds all down clues' text and numbers
	private ArrayList<String> gridEntriesList;		// List that holds all entries (box color, box text, box number) for the boxes on the grid
	
	// Variables used for general purposes, such as printing elapsed time and indexes to iterate over some lists
	private int hrs, mins, secs;
	private String hr, min, sec;
	private int acrossCluesListIndex, downCluesListIndex, gridEntriesListIndex;
	
	/**
	 * Create the panel Constructor
	 */
	public Panel() {
		setLayout(null);
		setPreferredSize(new Dimension(1400, 750));
		setBackground(new Color(248, 248, 255));
//		setBackground(Color.WHITE);
		buttonSelectedClue = null;
		dateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
		hrs = 0;
		mins = 0;
		secs = 0;
		acrossCluesListIndex = 0;
		downCluesListIndex = 0;
		gridEntriesListIndex = 0;
				
		// Reading required data from file and inserting them to the respective places 
		readAcrossClues();
		readDownClues();
		readGridEntries();
		
		/**
		 * Panels
		 */
		
		panelSelectedClue = new JPanel();
		panelSelectedClue.setBounds(25, 37, 541, 130);
		panelSelectedClue.setBackground(new Color(220, 239, 254));
		add(panelSelectedClue);
		panelSelectedClue.setLayout(null);
		
		panelMatrix = new JPanel() {
			public void paintComponent(Graphics g)
		    {
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        // Horizontal Grid Lines
		        g2.setStroke(new BasicStroke(2));
		        g2.drawLine(0, 108, 541, 108);
		        g2.drawLine(0, 216, 541, 216);
		        g2.drawLine(0, 324, 541, 324);
		        g2.drawLine(0, 432, 541, 432);
		        
		        // Vertical Grid Lines
		        g2.setStroke(new BasicStroke(2));
		        g2.drawLine(108, 0, 108, 541);
		        g2.drawLine(216, 0, 216, 541);
		        g2.drawLine(324, 0, 324, 541);
		        g2.drawLine(432, 0, 432, 541);
		        
		        g2.setStroke(new BasicStroke(5));
		        g2.drawRect(0, 0, 540, 540);
		        
		    }
		};
		panelMatrix.setBackground(Color.WHITE);
		panelMatrix.setBounds(25, 177, 541, 541);
		panelMatrix.setLayout(null);
		add(panelMatrix);
		
		/**
		 * Boxes
		 * Creating box objects which contains label, color, and answer letter
		 */
		grids = new ArrayList<Box>();
		int x_boxPanel = 1, y_boxPanel = 2, x_boxNo = 2, y_boxNo = 0, x_boxLetter = 27, y_boxLetter = 40, x_boxReveal = 72, y_boxReveal = 0, count = 1;
		for(int i = 0; i < 25; i++) {
			Box grid = new Box(x_boxPanel, y_boxPanel, x_boxNo, y_boxNo, x_boxLetter, y_boxLetter, x_boxReveal, y_boxReveal);
			grids.add(grid);
			if(count % 5 == 0) {
				x_boxPanel = 1;
				x_boxNo = 2;
				x_boxLetter = 27;
				x_boxReveal = 72;				
				y_boxPanel += 108;
			}
			else {
				x_boxPanel += 108;
			}
			count++;
		}
		
		// Create default grid, with black boxes (if any) and numbers properly
		for (int i = 0; i < 25; i++) {
			Box box = grids.get(i);
			String boxIsBlack = gridEntriesList.get(gridEntriesListIndex);
			String boxNo = gridEntriesList.get(gridEntriesListIndex + 2);
			
			if(boxIsBlack.equals("0")) {
				box.setPanelBackground(Color.BLACK);
				box.setIsBlack(true);
				box.setBoxNo(boxNo);
				box.setIsBlack(true);
			}
			else 
				box.setBoxNo(boxNo);
			
			gridEntriesListIndex += 3;
			panelMatrix.add(box.getBoxPanel());
		}
		
		
		/**
		 * Across Clues
		 * Generating buttons which contains across clues and their respective numbers
		 */
		int buttonAcrossYdir = 122; // Used to calculate Y direction of the across clue buttons
		buttonAcrossList = new ArrayList<JButton>();
		for(int i = 0; i < 5; i++) {
			JButton buttonAcross = new JButton();
			buttonAcross.setFont(new Font("Tahoma", Font.PLAIN, 16));
			buttonAcross.setBackground(Color.WHITE);
			buttonAcross.setBounds(624, buttonAcrossYdir, 345, 98);
			String acrossClueNumber = "<b>" + acrossCluesList.get(acrossCluesListIndex) + "</b>";
			acrossCluesListIndex++;
			String acrossClueText = acrossCluesList.get(acrossCluesListIndex);
			acrossCluesListIndex++;
			buttonAcross.setText("<html><left>" + acrossClueNumber + "\t" + acrossClueText + "</left></html>");
			buttonAcross.setBorderPainted( false );
			buttonAcross.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 if(buttonSelectedClue != buttonAcross && buttonSelectedClue != null)
		        		 buttonSelectedClue.setBackground(Color.WHITE);
		        	 buttonSelectedClue = buttonAcross;
		        	 buttonAcross.setBackground(new Color(167, 216, 255));
		        	 labelSelectedClue.setText("<html><left><b>" + acrossClueNumber + "A</b>\t" + acrossClueText + "</left></html>");
		         }
		      });
			add(buttonAcross);
			buttonAcrossList.add(buttonAcross);
			buttonAcrossYdir += 110;
		}
		buttonSelectedClue = buttonAcrossList.get(0);
		buttonSelectedClue.setBackground(new Color(167, 216, 255));
		
		
		/**
		 * Down Clues
		 * Generating buttons which contains down clues and their respective numbers
		 */
		
		int buttonDownYdir = 122; // Used to calculate Y direction of the down clue buttons
		buttonDownList = new ArrayList<JButton>();
		for(int i = 0; i < 5; i++) {
			JButton buttonDown = new JButton();
			buttonDown.setFont(new Font("Tahoma", Font.PLAIN, 16));
			buttonDown.setBackground(Color.WHITE);
			buttonDown.setBounds(1030, buttonDownYdir, 345, 98);
			String downClueNumber = "<b>" + downCluesList.get(downCluesListIndex) + "</b>";
			downCluesListIndex++;
			String downClueText = downCluesList.get(downCluesListIndex);
			downCluesListIndex++;
			buttonDown.setText("<html><left>" + downClueNumber + "\t" + downClueText + "</left></html>");
			buttonDown.setBorderPainted( false );
			buttonDown.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 if(buttonSelectedClue != buttonDown && buttonSelectedClue != null) 
		        		 buttonSelectedClue.setBackground(Color.WHITE);
		        	 buttonSelectedClue = buttonDown;
		        	 buttonDown.setBackground(new Color(167, 216, 255));
		        	 labelSelectedClue.setText("<html><left><b>" + downClueNumber + "D</b>\t" + downClueText + "</left></html>");
		         }
		      });
			add(buttonDown);
			buttonDownList.add(buttonDown);
			buttonDownYdir += 110;
		}
		
		
		/**
		 * Labels
		 */
		
		labelAccross = new JLabel("ACROSS");
		labelAccross.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelAccross.setBounds(603, 77, 86, 35);
		add(labelAccross);
		
		labelDown = new JLabel("DOWN");
		labelDown.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelDown.setBounds(1002, 77, 86, 35);
		add(labelDown);
		
		// Shows selected clue with specifying either it is an across clue or down clue using letters "A" and "D" respectively
		labelSelectedClue = new JLabel();
		labelSelectedClue.setFont(new Font("Tahoma", Font.PLAIN, 25));
		labelSelectedClue.setForeground(Color.BLACK);
		String labelSelectedClueNo = "<b>" + Integer.parseInt(buttonAcrossList.get(0).getText().substring(15,16)) + "A </b>";
		String labelSelectedClueText = buttonAcrossList.get(0).getText().substring(20);
		labelSelectedClue.setText("<html><left>" + labelSelectedClueNo + labelSelectedClueText + "</left></html>");
		labelSelectedClue.setBounds(23, 22, 484, 87);
		panelSelectedClue.add(labelSelectedClue);
		
		
		// Date and Time, showing today's date and time
		dateTimeGroupName = new JLabel("VERBATIM: " + dateFormat.format(Calendar.getInstance().getTime()));
		dateTimeGroupName.setBounds(389, 719, 175, 21);
		add(dateTimeGroupName);
		updateClock();
		new Timer(1000, this).start();
		
		// Shows time elapsed to solve the puzzle
		labelElapsedTime = new JLabel();
		labelElapsedTime.setBackground(Color.WHITE); // Maybe panel's color
		labelElapsedTime.setBounds(226, 14, 139, 13);
		add(labelElapsedTime);
		Timer  timer = new Timer(1000, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(mins != 0 && mins % 60 == 0) {
	    			mins = 0;
	    			hrs++;
	    		}
	    		if(secs != 0 && secs % 60 == 0) {
	    			secs = 0;
	    			mins++;
	    		}
	    		if(hrs < 10)
	    			hr = "0" + hrs;
	    		else
	    			hr = "" + hrs;
	    		if(mins < 10)
	    			min = "0" + mins;
	    		else
	    			min = "" + mins;
	    		if(secs < 10)
	    			sec = "0" + secs;
	    		else
	    			sec = "" + secs;	    			
	    		labelElapsedTime.setText("<html><center>" + hr +" : " + min + " : " + sec + "</center></html>");
	    		secs++;
	    	}
	    });
	    timer.start();
	    
		
		// Reveal Puzzle
		buttonRevealPuzzle = new JButton("REVEAL PUZZLE");
		buttonRevealPuzzle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		buttonRevealPuzzle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
//				System.out.println(timer.getActionCommand());
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to reveal the puzzle?", "Puzzle Reveal", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
					fillTheGrid();
					for(int i = 0; i < 5; i++) {
						buttonAcrossList.get(i).setEnabled(false);
						buttonAcrossList.get(i).setBackground(Color.WHITE);
						buttonDownList.get(i).setEnabled(false);
						buttonDownList.get(i).setBackground(Color.WHITE);
					}
					playMusic();
					JOptionPane.showMessageDialog(null, "You have solved the Mini puzzle in " + labelElapsedTime.getText().substring(14, 26) + "   ", "CONGRATULATIONS!", JOptionPane.INFORMATION_MESSAGE);
					buttonRevealPuzzle.setEnabled(false);
				}
				else
					timer.start();
			}
		});
		buttonRevealPuzzle.setForeground(Color.WHITE);
		buttonRevealPuzzle.setBackground(Color.BLACK);
		buttonRevealPuzzle.setBounds(603, 37, 175, 21);
		add(buttonRevealPuzzle);
		
		
		// This part automatically reveals the puzzle without any need of interaction
		timer.stop();
		fillTheGrid();
		for(int i = 0; i < 5; i++) {
			buttonAcrossList.get(i).setBackground(Color.WHITE);
			buttonDownList.get(i).setBackground(Color.WHITE);
		}
		buttonRevealPuzzle.setEnabled(false);
		playMusic();
			
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updateClock();		
	}
	
	// This method updates clock to show the current time
	private void updateClock() {
		dateTimeGroupName.setText("VERBATIM: " + dateFormat.format(Calendar.getInstance().getTime()));
	}
	
	// This method fills the grid with answers when the puzzle is revealed
	public void fillTheGrid() {
		gridEntriesListIndex = 0;
		for(int i = 0; i < grids.size(); i++) {
			String boxLetter = gridEntriesList.get(gridEntriesListIndex + 1);
			grids.get(i).setBoxLetter(boxLetter);
			if(!grids.get(i).getIsBlack())
				grids.get(i).setRevealVisible(true);
			gridEntriesListIndex += 3;
		}		
	}
	
	// This method reads across clues from the file, which is generated from the original puzzle
	public void readAcrossClues() {
		acrossCluesList = new ArrayList<String>();
		try {
			myFile = new File("filesFromWeb\\clues_across.txt");
			myScanner = new Scanner(myFile);
			while (myScanner.hasNextLine()) {
				String line = myScanner.nextLine();
				acrossCluesList.add(line);
				
			}
			myScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	    }
	}
	
	// This method reads down clues from the file, which is generated from the original puzzle
	public void readDownClues() {
		downCluesList = new ArrayList<String>();
		try {
			myFile = new File("filesFromWeb\\clues_down.txt");
			myScanner = new Scanner(myFile);
			while (myScanner.hasNextLine()) {
				String line = myScanner.nextLine();
				downCluesList.add(line);
			}
			myScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	    }
	}
	
	// This method reads grid entries (answers, black boxes, and box numbers) from the file, which is generated from the original puzzle
	public void readGridEntries() {
		gridEntriesList = new ArrayList<String>();
		try {
			myFile = new File("filesFromWeb\\answers.txt");
			myScanner = new Scanner(myFile);
			while (myScanner.hasNextLine()) {
				String line = myScanner.nextLine();
				String [] splittedLine = line.split("[,]", 0);
				if(splittedLine.length == 2) {
					gridEntriesList.add(splittedLine[1].substring(splittedLine[1].length()-2, splittedLine[1].length()-1));
					gridEntriesList.add("");
					gridEntriesList.add("");
				}
				else if(splittedLine.length == 3) {
					gridEntriesList.add(splittedLine[1].substring(splittedLine[1].length()-1));
					gridEntriesList.add(splittedLine[2].substring(splittedLine[2].length()-3, splittedLine[2].length()-2));
					gridEntriesList.add("");
				}
				else {
					gridEntriesList.add(splittedLine[1].substring(splittedLine[1].length()-1));
					gridEntriesList.add(splittedLine[2].substring(splittedLine[2].length()-2, splittedLine[2].length()-1));
					gridEntriesList.add(splittedLine[3].substring(splittedLine[3].length()-3, splittedLine[3].length()-2));
				}
				
			}
			myScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	    }
	}
	
	// Plays music at the end of the game
	public void playMusic() {
	    try {
	    	audioInputStream = AudioSystem.getAudioInputStream(new File("music\\endOfGameMusic.wav").getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing audio.");
	        ex.printStackTrace();
	    }
	}
}
