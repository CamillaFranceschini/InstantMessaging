package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import client.ClientCore;


@SuppressWarnings("serial")
public class LoginDialog extends JDialog {

	private JPanel panel;
	private JLabel jText1;
	private JLabel jText2;
	private JTextField username;
	private JTextField IP;
	private JButton login, cancel;
	
	public LoginDialog(JFrame parent){
		super(parent, "Login", true);
		
		//Creazione pannello
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setLayout(null);
		
		//Creazione e aggiunta dei componenti al pannello
		jText1 = new JLabel("Inserire un username:");
		jText1.setBounds(117, 125, 144, 16);
		jText1.setForeground(Color.WHITE);
		panel.add(jText1);
		
		username = new JTextField(20);
		username.setBounds(117, 143, 158, 26);
		username.setColumns(10);
		panel.add(username);
		username.validate();
		
		jText2 = new JLabel("Inserire IP server:");
		jText2.setBounds(117, 203, 144, 16);
		jText2.setForeground(Color.WHITE);
		panel.add(jText2);
		
		IP = new JTextField(20);
		IP.setBounds(117, 221, 158, 26);
		IP.setColumns(10);
		panel.add(IP);
		
		JLabel lblNewLabel_2 = new JLabel("Login");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBackground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Ayuthaya", Font.PLAIN, 27));
		lblNewLabel_2.setBounds(150, 16, 93, 53);
		panel.add(lblNewLabel_2);
		
		login = new JButton("Login");
		login.setBounds(56, 286, 111, 26);
		panel.add(login);
		
		
		cancel = new JButton("Annulla");
		cancel.setBounds(222, 285, 111, 29);
		panel.add(cancel);
		
		this.setContentPane(panel);
		this.setSize(500, 500);
		
		login.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//controllo che IP e username siano stati inseriti
				if (!username.getText().trim().contentEquals("")) {
					if(!IP.getText().trim().contentEquals("")) {
							ClientCore.setUsername(username.getText().trim());
							ClientCore.setIP(IP.getText().trim());
							dispose();

					}
				//errore IP non inserito
				else {
					JOptionPane.showMessageDialog(new JFrame(), "IP is blank. Try connecting again with a IP");
					}
				}
				//errore username non inserito
				else {
					JOptionPane.showMessageDialog(new JFrame(), "Username is blank. Try connecting again with a username");
				}
			}});
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); //simile a dispose()
			}});
	}
}