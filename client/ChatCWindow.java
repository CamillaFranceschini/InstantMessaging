package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import msg.Message;

public class ChatCWindow {

	private JFrame frame;
	private Container contentPane;
	private JPanel botPanel;
	private Border border;
	private JButton sendButton;
	private JScrollPane msgScroll, chatScroll;
	private JTextArea msgWindow, chatWindow;
	@SuppressWarnings("unused")
	private String recipient;
	
	private SimpleDateFormat sdf;
	
	public ChatCWindow(final String recipient, JFrame viewFrame){
		
		this.recipient = recipient;
		sdf = new SimpleDateFormat("HH:mm:ss");
		
		//Creazione del frame con relativo setting delle dimensioni
		frame = new JFrame("Chat with " + recipient);
		frame.setBackground(Color.GRAY);
		frame.setSize(600, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(viewFrame);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Creazione bordo
		border = BorderFactory.createLineBorder(Color.GRAY);
		
		//Crezione componenti e tasti
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		chatScroll = new JScrollPane(chatWindow);
		chatScroll.setBorder(border);
		
		sendButton = new JButton("Send");
		sendButton.setBackground(Color.GRAY);
		sendButton.setPreferredSize(new Dimension(50, 50));
		
		msgWindow = new JTextArea();
		msgScroll = new JScrollPane(msgWindow);
		msgScroll.setPreferredSize(new Dimension(500, 200));
		msgScroll.setBorder(border);
		botPanel = new JPanel();
		botPanel.setLayout(new BorderLayout());
		botPanel.add(msgScroll, BorderLayout.WEST);
		botPanel.add(sendButton);
		botPanel.setBorder(border);
		
		contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(2, 1));
		contentPane.add(chatScroll);
		contentPane.add(botPanel);
		
		frame.setContentPane(contentPane);
		frame.setVisible(true);
		
		//funzionamento sendButton
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String tempMsg = msgWindow.getText();
				msgWindow.setText("");
				sendMessage(recipient, tempMsg);
			}});
	}
	
	//Invio messaggi
	public void sendMessage(String recipient, String message){
		Date time = new Date();
		chatWindow.append(ClientCore.getUsername() + " [" + sdf.format(time) 
				+ "]: " + message + "\n");
		ClientCore.sendMessage(recipient, message, Message.MESSAGE);
	}
	
	//Ricezione messaggi
	public void receiveMessage(String sender, String message){
		Date time = new Date();
		chatWindow.append(sender + " [" + sdf.format(time) + "]: " 
				+ message + "\n");
	}
}