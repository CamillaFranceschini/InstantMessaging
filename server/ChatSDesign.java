package server;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

//Classe per la creazione della chat server
public class ChatSDesign {

	//Attributi
	private JFrame frame;
	private Container contentPane;
	private Border border;
	private JList<String> userList;
	private DefaultListModel<String> model;
	private JScrollPane scrollPane;
	public JTextArea serverMsgWindow;
	@SuppressWarnings("unused")
	private CSController controller;
	
	//Costruttore
	public ChatSDesign(final CSController controller){
		this.controller = controller;	
		
		//Creazione del frame con relativo setting delle dimensioni
		frame = new JFrame("Chat Server");
		frame.setBackground(Color.GRAY);
		frame.setSize(500, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creazione bordo
		border = BorderFactory.createLineBorder(Color.GRAY);
		
		//Creazione finestra Chatserver
		JPanel Title = new JPanel();
		Title.setBackground(Color.GRAY);
		JLabel Text = new JLabel("ChatServer");
		Text.setForeground(Color.WHITE);
		Text.setBackground(Color.WHITE);
		Text.setFont(new Font("Ayuthaya", Font.PLAIN, 27));
		Title.setLayout(null);
		Text.setBounds(170, -3, 200, 200);
		Title.add(Text);
		
		//creazione sezione per lista utenti
		model = new DefaultListModel<String>();
		userList = new JList<String>(model);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBorder(border);
		
		//Finestra per messaggi server con relativa barra di scorrimento
		serverMsgWindow = new JTextArea(10, 30);
		serverMsgWindow.setEditable(false);
		scrollPane = new JScrollPane(serverMsgWindow);
		scrollPane.setBorder(border);
		
		contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(3, 1));
		
		contentPane.add(Title);
		contentPane.add(userList);
		contentPane.add(scrollPane);
		
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}
	
	//Aggiorna l'elenco degli Utenti connessi
	public void updateUsers(String[] userArray){

		model.clear();
		
		for(int i = 0; i < userArray.length; i++){
			model.add(i, userArray[i]);
		}
	}
}