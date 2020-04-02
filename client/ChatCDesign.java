package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import msg.Message;
import server.CSHandler;

@SuppressWarnings("unused")
public class ChatCDesign {

	private JFrame frame;
	private Container contentPane;
	private Border border;
	private DefaultListModel<String> model;
	@SuppressWarnings("rawtypes")
	private JList userList;
	
	public ChatCDesign(){
		
		//Creazione del frame con relativo setting delle dimensioni
		frame = new JFrame("Chat Client");
		frame.setBackground(Color.GRAY);
		frame.setSize(300, 600);   
		frame.setResizable(true);   
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creazione bordo
		border = BorderFactory.createLineBorder(Color.GRAY);
		
		//Creazione contenuto
		contentPane = frame.getContentPane();
		
		//Creazione lista di utenti
		model = new DefaultListModel<String>();
		userList = new JList<String>(model);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBorder(border);
		
		contentPane.add(userList);
		
		frame.setContentPane(contentPane);
		frame.addWindowListener(new WindowAdapter(){
			//Metodo chiusura chat/finestre
			public void windowClosing(WindowEvent e){
				ClientCore.shutdown();
				System.exit(0);
			}
		});
		frame.setVisible(true);
		
		
		LoginDialog LoginDialog = new LoginDialog(frame);
		LoginDialog.getContentPane().setBackground(Color.GRAY);
		LoginDialog.setLocationRelativeTo(frame);
		LoginDialog.setBounds(100, 100, 390, 380);
		LoginDialog.setVisible(true);
		LoginDialog.setResizable(false);
		
		frame.setTitle(ClientCore.getUsername() + "'s Client");
		
		//Gestione eventi mouse
		userList.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
		    	if(e.getClickCount()==2){
		    		String recipient = (String) userList.getSelectedValue();
		    		ChatCWindow chatWindow = new ChatCWindow(recipient, frame);
		    		ClientCore.chats.put(recipient, chatWindow);
		        }
		    }
		});
	}
	//Metodo di aggiornamento degli utenti connessi
	public void updateUsers(String[] userArray){

		model.clear();
		
			for(int i = 0; i < userArray.length; i++){
			model.add(i, userArray[i]);
		}
	}
	//Metodo di creazione di nuove chat
	public void newChat(Message message) {
		ChatCWindow chatWindow = new ChatCWindow(message.getSender(), frame);
		ClientCore.chats.put(message.getSender(), chatWindow);
	}
}
