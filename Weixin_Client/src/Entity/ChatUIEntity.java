package Entity;

import Frame.ChatUI;

/**
* 
*/
public class ChatUIEntity {
	private ChatUI chatUI;
	private String name;
	
	public ChatUIEntity() {
		super();
	}
	
	public ChatUIEntity(ChatUI chatUI, String name) {
		super();
		this.chatUI = chatUI;
		this.name = name;
	}
	
	public ChatUI getChatUI() {
		return chatUI;
	}
	
	public void setChatUI(ChatUI chatUI) {
		this.chatUI = chatUI;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

