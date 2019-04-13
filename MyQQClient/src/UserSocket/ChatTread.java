package UserSocket;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import Entity.ChatUIEntity;
import Entity.User;
import Frame.ChatUI;
import Frame.FriendsUI;
import _Util.ChatUIList;
import _Util.CommandTranser;

/**
* @author zzz
* @version 创建时间：2018年7月4日 下午3:52:09
*/
public class ChatTread extends Thread{
	private Client client;
	private boolean isOnline = true; //没用 ------待删
	private User user; //如果同意好友请求， 则刷新好友列表
	private FriendsUI friendsUI; //刷新好友列表用
	private String username; //如果创建新的聊天窗口（chatUI)那么必须将username传进去 用来发送消息
	
	public ChatTread(Client client, User user, FriendsUI friendsUI) {
		this.client = client;
		this.user = user;
		this.friendsUI = friendsUI;
		this.username = user.getUsername();
		//this.chat_windows = chat_windows;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	//这里没用 以后删---------------------------------------
	public void setOnline(boolean isOnline) {
		 this.isOnline = true; 
	}
	
	//run()方法是不需要用户来调用的，当通过start方法启动一个线程之后，当线程获得了CPU执行时间，
	//便进入run方法体去执行具体的任务。注意，继承Thread类必须重写run方法，在run方法中定义具体要执行的任务
	@Override
	public void run() {
		if(!isOnline) {
			JOptionPane.showMessageDialog(null,  "unbelievable ！！！");
			return;
		}
		while(isOnline) {
			
			CommandTranser cmd = client.getData();
			//与服务器端相同处理接收到的消息(命令)
			//这里处理来自服务器的消息(命令)
			if(cmd != null) {
				
			 execute(cmd);
			 //System.out.println(cmd.getCmd());	
			}
		}
	}
	
	//处理消息(命令)
	private void execute(CommandTranser cmd) {
		//登录、忘记密码、注册消息未在此处处理
		System.out.println(cmd.getCmd());
		
		//聊天消息请求 
		if("message".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			//查询是否有与该好友的窗口该窗口
			String friendname = cmd.getSender();
			ChatUI chatUI = ChatUIList.getChatUI(friendname);
			if(chatUI == null) {
				chatUI = new ChatUI(username, friendname, username, client);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName(friendname);
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //如果以前创建过仅被别的窗口掩盖了 就重新显示
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yy-MM-dd hh:mm:ss a");
			String message = friendname + "说："
					+ (String) cmd.getData() + "\t" + sdf.format(date)
					+ "\n";
			chatUI.getChatWin().append(message); //追加消息
			return;
		}
		
		if("WorldChat".equals(cmd.getCmd())) {
//			if(cmd.isFlag() == false) {
//				JOptionPane.showMessageDialog(null, cmd.getResult()); 
//				return;
//			}
			//查询是否有与该好友的窗口该窗口
			String friendname = cmd.getSender();
			ChatUI chatUI = ChatUIList.getChatUI("WorldChat");
			if(chatUI == null) {
				chatUI = new ChatUI("WorldChat", "WorldChat", user.getUsername(), client);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName("WorldChat");
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //如果以前创建过仅被别的窗口掩盖了 就重新显示
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yy-MM-dd hh:mm:ss a");
			String message = friendname + "说："
					+ (String) cmd.getData() + "\t" + sdf.format(date)
					+ "\n";
			chatUI.getChatWin().append(message); //追加消息
			return;
		}
		
		if("requeste_add_friend".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			String sendername = cmd.getSender();
			int flag = JOptionPane.showConfirmDialog(null, "是否同意" + sendername + "的好友请求", "好友请求", JOptionPane.YES_NO_OPTION);
			System.out.println(flag);
			if(flag == 0) {
				cmd.setCmd("accept_add_friend");
			} else {
				cmd.setCmd("refuse_add_friend");			
			}
			cmd.setSender(username);
			cmd.setReceiver(sendername);
			client.sendData(cmd);
			return;
		}
		
//		if("successful".equals(cmd.getCmd())) {
//			JOptionPane.showMessageDialog(null, cmd.getResult()); 
//			return;
//		}
		
		if("accept_add_friend".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			
//			CommandTranser newcmd = new CommandTranser();
//			newcmd.setCmd("updatefriendlist");
//			newcmd.setReceiver(username);
//			newcmd.setSender(username);
//			newcmd.setData(user);
//			client.sendData(cmd);
			
			return;
			
		}
		
//		if("updatefriendlist".equals(cmd.getCmd())) {
//			if(cmd.isFlag() == false) {
//				JOptionPane.showMessageDialog(null, cmd.getResult()); 
//				return;
//			}
//			
//			User tmp = (User)cmd.getData();
//			user.setFriendsList(tmp.getFriend());
//			friendsUI.validate();
//			friendsUI.repaint();
//			friendsUI.setVisible(true);
//			
//			return;
//		}
		
		if("refuse_to_add".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			return;
		}
		
		if("changepwd".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			return;
		}
		return;
	}
	
}

