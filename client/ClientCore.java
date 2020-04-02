package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import msg.Message;

public class ClientCore {

	final static int portNumber = 40;  	//final static = non puoi cambiare il valore della variabile 
	public static String hostName = "";
	private static ChatCDesign view;				//Visualizzazione schermata chat
	private static ObjectInputStream cObjIn;
	private static ObjectOutputStream cObjOut;
	private static Socket socket;
	private static String username;
	public static HashMap<String, ChatCWindow> chats;
	public static Queue<Message> msgQueue;
	
	public ClientCore() {
		
		view = new ChatCDesign();
		try {
			connect(); 				//richiamo la funzione connect()
		} catch (IOException e) {		
			e.printStackTrace();		//se non riesce a connettersi, scrive l'errore e (riga 81)
		}
		msgQueue = new LinkedList<Message>();			//mantiene nella cache la lista dei messaggi (?)
		chats = new HashMap<String, ChatCWindow>();		//HashMap => contenitore di coppie chiave-valore da 16 elementi
		
		Listener chatListener = new Listener();
		chatListener.start();
	}
	
	public static void connect() throws IOException{
		try {
			socket = new Socket(hostName, portNumber);
			
			System.out.println("Trying connecting to server.");

			//definisce input e output su quell'indirizzo
			cObjOut = new ObjectOutputStream(socket.getOutputStream());
			cObjIn = new ObjectInputStream(socket.getInputStream());

			//Tentativo di login
			cObjOut.writeObject(new Message(null, null, username, Message.LOGIN));
			cObjOut.flush();

			Message confirmation;

			confirmation = (Message) cObjIn.readObject();
			//connessione avvenuta con successo
			if(confirmation.getData().equals("SUCCESS")){
				
				System.out.println("Connection successful.");
				//Aggiornamento degli utenti
				
				Message users = (Message) cObjIn.readObject();
				view.updateUsers((String[]) users.getData());
			}
			//username già in utilizzo
			else if(confirmation.getData().equals("USERNAME_ERROR")) {
				JOptionPane.showMessageDialog(new JFrame(), "Username is already in use. Try connecting again with another username");
				System.out.println("Username is already taken. Try connecting again with another username");
				System.exit(0);
			}
			else{
				System.out.println("Connection failed.");
				JOptionPane.showMessageDialog(new JFrame(), "Connection Failed");
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Wrong class returned.");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.err.println("Host " + hostName + " and Port " + portNumber
					+ " could not be resolved.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to establish I/O connection with " +
					hostName + " and Port " + portNumber);
			e.printStackTrace();
		}
	}	
	
	//Parse Message -- determina il tipo di messaggi e agisce di conseguenza 
	//Riceve in input un oggetto di tipo Message e lo analizza
	public void parseMessage(Message message){
		try {
			switch (message.getType()){ 	
			case Message.MESSAGE: receiveMessage(message);		//stampa il messaggio	
				break;
			case Message.UPDATE_USERS: view.updateUsers((String[]) message.getData());		//aggiorna l'elenco di utenti all'interno della chat
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Ricezione messaggi
	public void receiveMessage(Message message){
		
		//Controllo se il sender è presente nell'elenco, se non c'è apro una nuova chat
		if(chats.containsKey(message.getSender())){ 
			//Stampa il sender, la data di invio e il messaggio
			chats.get(message.getSender()).receiveMessage(message.getSender(), (String) message.getData());
		}
		else{
			view.newChat(message);
			//Stampa il sender, la data di invio e il messaggio
			chats.get(message.getSender()).receiveMessage(message.getSender(), (String) message.getData());
		}
	}
	
	//Invio messaggi
	public static void sendMessage(String recipient, String message, int type){
		Message msg = new Message(recipient, username, message, type);
		try {
			cObjOut.writeObject(msg);
			cObjOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setUsername(String user){
		username = user;
	}
	
	public static void setIP(String IP){
		hostName = IP;
	}
	
	public static String getUsername(){
		return username;
	}
	
	public static void shutdown(){
		sendMessage(null, "LOGOUT", Message.LOGOUT);
	}
	
	public static void main(String[] args) throws IOException{
		@SuppressWarnings("unused")
		ClientCore client = new ClientCore();
	}
	
	public class Listener extends Thread {

		public synchronized void run(){			
			try{
				while(true){
					msgQueue.add((Message)cObjIn.readObject()); 	//Aggiunge i messaggi alla lista sqQueue
					//Parse = analisi
					parseMessage(msgQueue.poll());					//Recupera e rimuove ogni elemtno della lista a partire dal primo
				}
			} catch(Exception e){		//Scrive l'errore se non funziona
				e.printStackTrace();
			}
		}
	}
}






