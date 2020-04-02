package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


import msg.Message;


//Definizione classe ChatServer
public class ServerCore {

	final static int portNumber = 40;
	private static ServerSocket serverSocket;
	private static InetAddress IP;
	private static Socket clientSocket;
	public static Map<String, CSHandler> users = new HashMap<String, CSHandler>();
	public static Queue<Message> msgQueue;
	private static Listener serverListener;
	
	private static ChatSDesign design;
	private static CSController controller;
	
	//Costruttore
	public ServerCore(){
		
		controller = new CSController();
		design = new ChatSDesign(controller);
		controller.setChatSDesign(design);

		msgQueue = new LinkedList<Message>();
		
		init();				//Inizializza il chat server
		
		serverListener = new Listener();
		serverListener.start();
		
		while(true){
			listen();		//server in ascolto
		}
		
	}
	
	//Inizializzazione del server
	public static void init(){
		try{
			//Start server
			IP = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(portNumber);
			controller.writeMsg("Server running on " + IP + ":" + portNumber);
			
			//Debug
			System.out.println("Server running on " + IP + ":" + portNumber);
		} catch(Exception e){
			System.err.println("Server could not be started.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Il server resta in ascolto e permette il login ai nuovi utenti
	public static void listen(){
		
		try{
			//Stabilisce una connessione tra le due
				clientSocket = serverSocket.accept();
				
				loginUser(clientSocket);
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//funzione di login per i nuovi utenti
	public static void loginUser(Socket clientSocket) throws IOException{

		try{
			ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
			Message loginRequest = (Message) objIn.readObject();
			
			
			if(loginRequest.getType() == Message.LOGIN && !loginRequest.getData().equals("")){
				//controllo se l'username inserito è già utilizzato
				int i = 0;
				for ( String key : users.keySet() ) {
					if (key.equals((String) loginRequest.getData())) {
						i++;
					}
				}
				if (i>0) {
					objOut.writeObject(new Message(null, "Server", "USERNAME_ERROR", Message.SERVER_RESPONSE));
					objOut.flush();
				}
				else {
					//Crea un nuovo handler
					CSHandler handler = new CSHandler(clientSocket, objIn, objOut);
					//Aggiunge l'utente all'hashmap delle chat
					users.put((String) loginRequest.getData(), handler);
					//Conferma del login
					controller.writeMsg("User " + loginRequest.getData() + " logged in");
						
					//Conferma all'utente la connessione
					objOut.writeObject(new Message(null, "Server", "SUCCESS", Message.SERVER_RESPONSE));
					objOut.flush();
						
					//Comunica l'entrata del nuovo utente a tutti gli utenti già connessi
					updateAllUsers();
						
					//Aggiorna l'elenco degli utenti nella grafica del server
					design.updateUsers(getUsernames());
				}	
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();	
		}
	}
	
	//Crea un array per scrivere l'elenco degli utenti
	public static String[] getUsernames(){
		return users.keySet().toArray(new String[users.size()]);
	}
	
	//Aggiorna il server con i nomi degli user presenti nell'array users creato nella funzione getUsernames
	public static void updateAllUsers(){
		for(String user : users.keySet()){
			users.get(user).sendMessage(new Message(user, "Server", getUsernames(), Message.UPDATE_USERS));
		}
	}
	
	public static void removeUser(String user){
		users.remove(user);
		controller.writeMsg("User " + user + " has logged out.");
		updateAllUsers();
		design.updateUsers(getUsernames());
	}
	
	public static void main(String[] args){
		
		@SuppressWarnings("unused")
		ServerCore server = new ServerCore();
	}
	
	public static Listener getListener(){
		return serverListener;
	}
	

	public class Listener extends Thread {
		
		public synchronized void run(){
			try{
				while(true){
					//Se msgQueue è vuota, aspetta
					while(msgQueue.isEmpty()){
						wait();
					}
					
					//currentMsg diventa uguale al primo elemento di msgQueue
					Message currentMsg = msgQueue.poll();
					
					//Trova il destinatario corrispondente al messaggio e lo invia
					for(String user : users.keySet()){
						if(user.equals(currentMsg.getRecipient())){
							users.get(user).sendMessage(currentMsg);
						}
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public synchronized void addMsg(Message message){
			msgQueue.add(message);
			notify();
		}
	}
}
