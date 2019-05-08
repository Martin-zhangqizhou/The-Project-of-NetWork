package UserSocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import Entity.ChatUIEntity;
import Entity.User;
import Frame.ChatUI;
import Frame.FriendsUI;
import _Util.ChatUIList;
import _Util.CommandTranser;
import java.util.*;
import java.net.*;
 
public class ChatTread extends Thread{
	private Client client;
	private boolean isOnline = true; //没用 ------待删
	private User user; //如果同意好友请求， 则刷新好友列表
	private FriendsUI friendsUI; //刷新好友列表用
	private String username; //如果创建新的聊天窗口（chatUI)那么必须将username传进去 用来发送消息
	private StyledDocument doc; 
	InetAddress ip;// = InetAddress.getLocalHost();
	public ChatTread(Client client, User user, FriendsUI friendsUI) throws UnknownHostException {
		this.client = client;
		this.user = user;
		this.friendsUI = friendsUI;
		this.username = user.getUsername();
		ip = InetAddress.getLocalHost();
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
			//System.out.println("在线");
			CommandTranser cmd = client.getData();
			//与服务器端相同处理接收到的消息(命令)
			//这里处理来自服务器的消息(命令)
			if(cmd != null) {
				
				 try {
					execute(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 //System.out.println(cmd.getCmd());	
			}
		}
	}
	
	//处理消息(命令)
	@SuppressWarnings("null")
	private void execute(CommandTranser cmd) throws IOException {
		//登录、忘记密码、注册消息未在此处处理
		//System.out.println("execute");
		System.out.println(cmd.getReceiver()+"收到的命令为："+cmd.getCmd());
		System.out.println("数据类型为："+cmd.getData().getClass().toString());
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
				chatUI = new ChatUI(username, friendname, username, client, user);
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
			JTextPane chat_windows = chatUI.getChatWin();
			doc = chat_windows.getStyledDocument();
			String s = "123";
			ImageIcon i = null;
			System.out.println("s的数据类型为："+s.getClass().toString());
			
			if(cmd.getData().getClass().toString().equals(s.getClass().toString())){//文字
				String message = friendname + "说："
						+ (String) cmd.getData() + "\t" + sdf.format(date)
						+ "\n";
				try {
					doc.insertString(doc.getLength(), message, null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				chat_windows.setCaretPosition(doc.getLength());//用来接到后面的位置 
			}else  {
				try {
					doc.insertString(doc.getLength(), friendname + "说：", null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ImageIcon I = (ImageIcon) cmd.getData();
				chat_windows.setCaretPosition(doc.getLength());//用来接到后面的位置  
			    chat_windows.insertIcon(I);
			    doc = chat_windows.getStyledDocument();
				try {
					doc.insertString(doc.getLength(), sdf.format(date) + "\n", null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			return;
		}
		
		if("not_online".equals(cmd.getCmd())){
			JOptionPane.showMessageDialog(null,  "对方不在线，请稍后再发！");
		}
		
		if("all_online".equals(cmd.getCmd())){
			System.out.println("客户端收到所有在线人员为"+cmd.getAllOnline());
			ArrayList<String> all_online = cmd.getAllOnline();
			user.setAllOnline(all_online);
			
			//更新在线用户
			friendsUI.dispose();//关闭原来的窗口
			System.out.println("更新窗口");
			FriendsUI friendsUI = new FriendsUI(user, client);
			//使用 validate 方法会使容器再次布置其子组件。已经布置容器后，在修改此容器的子组件的时候
			//（在容器中添加或移除组件，或者更改与布局相关的信息），应该调用上述方法。
			friendsUI.validate();
			friendsUI.repaint();
			friendsUI.setVisible(true);
			this.friendsUI = friendsUI;
			return;
		}
		
		if("WorldChat".equals(cmd.getCmd())) {
//			if(cmd.isFlag() == false) {
//				JOptionPane.showMessageDialog(null, cmd.getResult()); 
//				return;
//			}
			//查询是否有与该好友的窗口该窗口
			String friendname = cmd.getSender();
			String sender = cmd.getSender();
			String receiver = cmd.getReceiver();
			ChatUI chatUI = ChatUIList.getChatUI(sender);
			
			if(chatUI == null) {
				chatUI = new ChatUI(receiver, sender, user.getUsername(), client, user);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName(sender);
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
			JTextPane chat_windows = chatUI.getChatWin();
			doc = chat_windows.getStyledDocument();
			try {
				doc.insertString(doc.getLength(), message, null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chat_windows.setCaretPosition(doc.getLength());//用来接到后面的位置 
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
			//JOptionPane.showMessageDialog(null, cmd.getResult());
			System.out.println(cmd.getResult()+" 接收到添加命令");
			CommandTranser newcmd = new CommandTranser();
			System.out.println("接受好友："+username);
			newcmd.setCmd("updatefriendlist");
			newcmd.setReceiver(username);
			newcmd.setSender(username);
			newcmd.setData(user);
			client.sendData(newcmd);
			return;
		}
		
		if("updatefriendlist".equals(cmd.getCmd())) {
			System.out.println(cmd.getCmd()+"判断");
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			User tmp = (User)cmd.getData();
			System.out.println(tmp.getUserpwd()+" 接收到更新命令");
			System.out.println(tmp.getFriend());
			user.setFriendsList(tmp.getFriend());
			//更新好友列表
			friendsUI.dispose();//关闭原来的窗口
			FriendsUI friendsUI = new FriendsUI(user, client);
			//使用 validate 方法会使容器再次布置其子组件。已经布置容器后，在修改此容器的子组件的时候
			//（在容器中添加或移除组件，或者更改与布局相关的信息），应该调用上述方法。
			friendsUI.validate();
			friendsUI.repaint();
			friendsUI.setVisible(true);
			this.friendsUI = friendsUI;
			return;
		}
		
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

