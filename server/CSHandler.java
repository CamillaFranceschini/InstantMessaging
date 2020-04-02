package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import msg.Message;

public class CSHandler extends Thread {

	@SuppressWarnings("unused")
	private Socket socket;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	
	//Costruttore
	public CSHandler(Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut){
		this.socket = socket;
		this.objIn = objIn;
		this.objOut = objOut;
		start();
	}
	
	//Intercetta i messaggi in arrivo dal client e li mette in coda
	public synchronized void run(){
		while(true){
			
			try {
				
				Message incoming = (Message) objIn.readObject();
				
				if(incoming.getType() == Message.MESSAGE){
					//Aggiunge alla lista il messaggio
					ServerCore.getListener().addMsg(incoming);
				}
				else if(incoming.getType() == Message.LOGOUT){
					//Rimuove l'utente che ha mandato il messaggio di logout
					ServerCore.removeUser(incoming.getSender());
				}
				
			} catch (java.net.SocketException e){
				System.err.println("User disconnected");
				
				try {
					//Chiusura degli stream
					objIn.close();
					objOut.close();
					break;
				} catch (IOException e1) {/*So many try catches*/}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(Message message) {
		try {
			objOut.writeObject(message);
			objOut.flush();
		} catch (IOException e) {
			System.err.println("Message from " + message.getSender() + " to " + 
					message.getRecipient() + " ran into some issues.");
			e.printStackTrace();
		}
		
	}
}
