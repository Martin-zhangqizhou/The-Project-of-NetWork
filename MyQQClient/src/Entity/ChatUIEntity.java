package Entity;

import Frame.ChatUI;

/**
* @author zzz
* @version 创建时间：2018年7月6日 下午4:54:42
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

