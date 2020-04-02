package msg;

import java.io.Serializable;

@SuppressWarnings("serial")				//elimina errori e warnings
//Classe Message
public class Message implements Serializable {

	//Tipi di messaggio
	public static final int MESSAGE = 0;
	public static final int LOGIN = 1;
	public static final int LOGOUT = 2;
	public static final int UPDATE_USERS = 3;
	public static final int SERVER_RESPONSE = 4;
	
	//Attributi generici
	private String recipient;	//destinatario messaggio
	private String sender;		//mittente messaggio
	private Object data;		//contenuto messaggio
	private int type;			//tipo messaggio (riga 9-13)
	
	//Costruttore classe Message 
	public Message(String recipient, String sender, Object data, int type) {
		this.recipient = recipient;
		this.sender = sender;
		this.data = data;
		this.type = type;
	}
	
	//Funzioni 
	public String getRecipient() {
		return recipient;
	}

	public String getSender() {
		return sender;
	}

	public Object getData() {
		return data;
	}


	public int getType() {
		return type;
	}
}
