package _Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
*  
*/
public class CommandTranser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sender = null;// 发送者
	private String receiver = null;// 接受者
	private Object data = null;// 传递的数据
	private boolean flag = false;// 指令的处理结果
	private String cmd = null;// 服务端要做的指令
	private String result = null;// 处理结果
	private ArrayList<String> friends = null;//发送者的朋友列表
	private ArrayList<String> all_online = null;//所有在线的人
	
	public String getSender() {
		return sender;
	}

	public String setSender(String sender) {
		return this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String setReceiver(String receiver) {
		return this.receiver = receiver;
	}

	public Object getData() {
		return data;
	}

	public Object setData(Object data) {
		return this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public boolean setFlag(boolean flag) {
		return this.flag = flag;
	}

	public String getResult() {
		return result;
	}

	public String setResult(String result) {
		return this.result = result;
	}

	public String getCmd() {
		return cmd;
	}

	public String setCmd(String cmd) {
		return this.cmd = cmd;
	}

	public void setFriends(ArrayList<String> friends){
		this.friends = friends;
	}
	
	public ArrayList<String> getFriends(){
		return friends;
	}
	
	public ArrayList<String> setAllOnline(ArrayList<String> all_online){
		return this.all_online = all_online;
	}
	
	public ArrayList<String> getAllOnline(){
		return all_online;
	}
}

