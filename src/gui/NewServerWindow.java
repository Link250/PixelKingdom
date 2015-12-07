package gui;

import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gfx.SpriteSheet;
import gui.menu.ServerList;
import main.Game;

public class NewServerWindow extends JFrame {
	private static final long serialVersionUID = 9218100846608549089L;
	private static final int WIDTH = 400, HEIGHT = 130;
	
	private Label nameLabel, ipLabel;
	private JTextField nameField, ipField;
	private JButton addButton, cancelButton;
	
	public NewServerWindow(Game game, ServerList serverList) {
		this.setSize(WIDTH+10, HEIGHT+30);
		this.setResizable(false);
		this.setLayout(null);
		
		this.setTitle("Adding a new Server");
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(game);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent windowEvent){
				dispose();
			}
		});
		
		try {this.setIconImage(ImageIO.read(SpriteSheet.class.getResourceAsStream("/WindowIcon.png")));} catch (IOException e) {e.printStackTrace();}
		
		nameLabel = new Label();
		nameLabel.setBounds(10, 10, 140, 30);
		nameLabel.setFont(new Font("Arial",0,20));
		nameLabel.setText("Server Name :");
		this.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setBounds(150, 10, 240, 30);
		nameField.setFont(new Font("Arial",0,20));
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {checkTextFields();}
			public void insertUpdate(DocumentEvent arg0) {checkTextFields();}
			public void removeUpdate(DocumentEvent arg0) {checkTextFields();}
		});
		this.add(nameField);
		
		ipLabel = new Label();
		ipLabel.setBounds(10, 50, 140, 30);
		ipLabel.setFont(new Font("Arial",0,20));
		ipLabel.setText("Server IP :");
		this.add(ipLabel);
		
		ipField = new JTextField();
		ipField.setBounds(150, 50, 240, 30);
		ipField.setFont(new Font("Arial",0,20));
		ipField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {checkTextFields();}
			public void insertUpdate(DocumentEvent arg0) {checkTextFields();}
			public void removeUpdate(DocumentEvent arg0) {checkTextFields();}
		});
		this.add(ipField);
		
		addButton = new JButton();
		addButton.setBounds(10, 90, WIDTH/2-20, 30);
		addButton.setFont(new Font("Arial",0,20));
		addButton.setText("Add Server");
		addButton.setEnabled(false);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverList.addServer(nameField.getText(), ipField.getText());
				dispose();
			}
		});
		this.add(addButton);
		
		cancelButton = new JButton();
		cancelButton.setBounds(WIDTH/2+10, 90, WIDTH/2-20, 30);
		cancelButton.setFont(new Font("Arial",0,20));
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		this.add(cancelButton);
		
		this.setVisible(true);
	}
	
	private void checkTextFields() {
		addButton.setEnabled(nameField.getText().length()>0 && ipField.getText().length()>0);
	}
}
