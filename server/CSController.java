package server;

//Creazione classe CSController (attributo di ChatSDesign e ChatServer)
public class CSController {
	
	//Attributi
	private ChatSDesign design;
	
	public CSController(){}
	
	public void setChatSDesign(ChatSDesign design){
		this.design = design;
	}
	
	//Fa apparire i messaggi all'interno della finestra server
	public void writeMsg(String string){
		design.serverMsgWindow.append(">>> " + string + "\n");
	}
}
