package ui;

import java.awt.EventQueue;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Demo {
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException{
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(60, 12, 1200, 750);		
		Panel panel = new Panel();
		
		frame.add(panel);
		frame.setTitle("The Mini Crossword");
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

}
