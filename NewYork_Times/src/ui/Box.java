package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Box {
	
	private JPanel panelBox; 
	private JLabel labelBoxNo;
	private JLabel labelBoxLetter;
	private JLabel labelBoxReveal;
	private boolean isBlack;
		
	public Box(int x_boxPanel, int y_boxPanel, int x_boxNo, int y_boxNo, int x_boxLetter, int y_boxLetter, int x_boxReveal, int y_boxReveal) {
		
		panelBox = new JPanel();
		panelBox.setLayout(null);
		panelBox.setBounds(x_boxPanel, y_boxPanel, 106, 106);
		panelBox.setBackground(Color.WHITE);
		
		labelBoxNo = new JLabel();
		labelBoxNo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		labelBoxNo.setBounds(x_boxNo, y_boxNo, 40, 40);
		
		labelBoxLetter = new JLabel("");
		labelBoxLetter.setFont(new Font("Tahoma", Font.PLAIN, 60));
		labelBoxLetter.setBounds(x_boxLetter, y_boxLetter, 65, 65);
		labelBoxLetter.setForeground(new Color(54, 108, 240));
		
		labelBoxReveal = new JLabel();
		labelBoxReveal.setIcon(new ImageIcon("E:\\Bilkent\\Bilkent 5th year\\2nd semester\\CS 461\\Demo\\Demo2\\images\\reveal_Icon.JPG"));
		labelBoxReveal.setBounds(x_boxReveal, y_boxReveal, 35, 35);
		labelBoxReveal.setVisible(false);
		
		
		panelBox.add(labelBoxNo);
		panelBox.add(labelBoxLetter);
		panelBox.add(labelBoxReveal);
		
		isBlack = false;
	}
	
	public void setPanelBackground(Color backgroundColor) {
		panelBox.setBackground(backgroundColor);
	}
	
	public JPanel getBoxPanel() {
		return panelBox;
	}
	
	public void setBoxNo(String no){
		labelBoxNo.setText(no);
	}
	
	public String getBoxNo(){
		return labelBoxNo.getText();
	}
	
	public void setBoxLetter(String letter){
		labelBoxLetter.setText("<html><center>" + letter + "</center></html>");
	}
	
	public String getBoxLetter(){
		return labelBoxLetter.getText();
	}
	
		
	public void setRevealVisible(boolean visible) {
		labelBoxReveal.setVisible(visible);
	}
	
	public void setIsBlack(boolean black) {
		isBlack = black;
	}

	public boolean getIsBlack() {
		return isBlack;
	}
}
