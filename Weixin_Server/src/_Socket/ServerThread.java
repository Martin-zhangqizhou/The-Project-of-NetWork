package _Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import Entity.SocketEntity;
import Entity.User;
import _Service.UserService;
import _Util.CommandTranser;
import _Util.SocketList;
import java.util.Set;
/**
 * 该类为多线程类，用于服务端处理多客户端请求
 */
public class ServerThread extends Thread{
	private Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		ObjectOutputStream oos1 = null;//代表发送方（同意加好友除外）
		ObjectOutputStream oos2 = null;
		//ObjectOutputStream oos3 = null;
		
		while(socket != null) {
			try {
				//将接收的流转化为命令
				//oos1 = null;
				oos2 = null;
				ois = new ObjectInputStream(socket.getInputStream());
				CommandTranser cmd = (CommandTranser) ois.readObject();
				//System.out.println("服务器收到的数据类型："+cmd.getData().getClass());
				//执行命令来自客户端的请求
				cmd = execute(cmd);//解析得服务器的cmd
				System.out.println("服务器收到的客户端命令: " + cmd.getCmd());
				System.out.println("收到数据："+cmd.getData());
				//消息对话请求，服务器将sender发来的消息发送给receiver
				if("message".equals(cmd.getCmd())) {
					//如果 msg.ifFlag即 服务器处理成功 可以向朋友发送信息 如果服务器处理信息失败 信息发送给发送者本人
					if(cmd.isFlag()) {//判断接收者是否在线
						System.out.println("对方" + cmd.getReceiver() + "在线");
						//cmd.getReceiver()获取接收者的昵称，然后getSocket获取相应的socket，然后getOutputStream()建立到客户端的连接
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					} else {
						//JOptionPane.showMessageDialog(null,  "对方不在线，请稍后再发！");
						System.out.println("对方未在线");
						//此处的socket为发送者的socket
						cmd.setCmd("not_online");
						String sender = cmd.getSender();
						cmd.setReceiver(sender);
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getSender()).getOutputStream());

						//oos2 = new ObjectOutputStream(SocketList.getSocket(cmd.getSender()).getOutputStream());
					}
				}
				
				if ("WorldChat".equals(cmd.getCmd())) {
					HashMap<String, Socket> map = SocketList.getMap();//获取所有服务器上的在线的socket
					System.out.println(cmd.getFriends());
					ArrayList<String> friends = cmd.getFriends();
					//通过Map.entrySet使用iterator遍历key和value
					Iterator<Map.Entry<String, Socket>> it = map.entrySet().iterator();
					while(it.hasNext()) {//key是客户端的名称，value为对应的socket
						Map.Entry<String, Socket> entry = it.next();
						if(friends.contains(entry.getKey())) {
							cmd.setReceiver(entry.getKey());
							oos1 = new ObjectOutputStream(entry.getValue().getOutputStream());
							oos1.writeObject(cmd);
						}
						/*
						if(!entry.getKey().equals(cmd.getSender())) {
							oos1 = new ObjectOutputStream(entry.getValue().getOutputStream());
							oos1.writeObject(cmd);
						}*/
					}
					continue;
				}
				if("all_online".equals(cmd.getCmd())){
					System.out.println("所有在线人员"+cmd.getAllOnline());
					ArrayList<String> all_online = cmd.getAllOnline();
					/*
					ArrayList<String> net_online = new ArrayList();
					Iterator it = all_online.iterator();
					String owner = SocketList.getNet(cmd.getSender());
					//在在线用户中筛选出局域网中用户
					while(it.hasNext()){
						String name = (String)it.next();
						String net = SocketList.getNet(name);
						System.out.println(name+" "+net);
						if(net.equals(owner)){
							net_online.add(name);
						}
					}
					cmd.setAllOnline(net_online);*/
					System.out.println("所有局域网在线人员"+cmd.getAllOnline());
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//登录请求 将数据发送给sender
				if ("login".equals(cmd.getCmd())) {
					System.out.println("登录");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//注册请求 将数据发送给sender
				if ("register".equals(cmd.getCmd())) {
					System.out.println("注册");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				
				//添加好友请求将数据发送给 receiver
				if ("requeste_add_friend".equals(cmd.getCmd())) {
					//在线，将请求发给receiver
					System.out.println(cmd.getSender() + " 请求加 " + cmd.getReceiver() + " 的好友");
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
//						oos3 = new ObjectOutputStream(socket.getOutputStream());
//						CommandTranser newcmd =  new CommandTranser();
//						newcmd = cmd;
//						newcmd.setCmd("successful");
//						oos3.writeObject(newcmd);
					} else {
						//不管在不在线都要向发送方提示消息发送成功
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				//同意添加好友请求将数据发送给 receiver和sender
				if ("accept_add_friend".equals(cmd.getCmd())) {
					//无论是否成功插入数据库都要将结果反馈，但有可能最初请求的客户下线了
					System.out.println(cmd.getReceiver()+ " 同意加 "+ cmd.getSender());
					oos1 = new ObjectOutputStream(socket.getOutputStream());
					if(SocketList.getSocket(cmd.getReceiver()) != null) {
						oos2 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					}
				}
				
				if("updatefriendlist".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//拒绝添加好友请求将数据发送给 receiver
				if ("refuse_to_add".equals(cmd.getCmd())) {
					//被拒绝方在线
					System.out.println(cmd.getReceiver()+ " 拒绝加 "+ cmd.getSender());
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					}else { //被拒方不在线则向拒绝方发送消息
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				//修改资料请求 发送给sender 功能暂未实现
				if ("changeinfo".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//修改密码请求 将数据发送给sender
				if ("changepwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//忘记密码 发送给sender
				if ("forgetpwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//用户下线
				if("logout".equals(cmd.getCmd())) {
					return;
				}
				
				if(oos1 != null) {
					//System.out.println("oos1"+oos1);
					oos1.writeObject(cmd);	
					//oos1 = null;
				}
				if(oos2 != null) {
					//System.out.println("oos2"+oos2);
					oos2.writeObject(cmd);	
					//oos2 = null;
				}
			} catch(IOException e) {
				socket = null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//处理客户端发来的命令
	private CommandTranser execute(CommandTranser cmd) {
		
		//登录请求
		System.out.println("服务器" + cmd.getCmd());
		if("login".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();//获取传送过来的数据
			System.out.println("login登录："+user.getUsername());
			cmd.setFlag(userservice.checkUser(user));
			//如果登陆成功，将该客户端加入已经连接成功的map集合里面 并且开启此用户的接受线程
			
			if(cmd.isFlag()) {
				// 将该线程加入连接成功的map集合
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				
				//从数据库获取其好友列表并将其好友列表发送至客户端
				cmd.setData(userservice.getFriendsList(user));//控制好友列表
				cmd.setResult("登陆成功");
			} else {
				cmd.setResult("密码错误");
			}
		}
		
		//注册请求
		if("register".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setResult("hello");
			cmd.setFlag(userservice.registerUser(user));
			//如果注册成功
			if(cmd.isFlag()) {
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//刚注册的肯定没有好友 
				cmd.setResult("注册成功");
			} else {
				cmd.setResult("注册失败");
			}
		}
		
		if("all_online".equals(cmd.getCmd())){
			System.out.println("all_online is here");
			//Set all_online = SocketList.getAllOnline();
			ArrayList<String> all_online = new ArrayList<String>(SocketList.getAllOnline());
			System.out.println(all_online);
			cmd.setAllOnline(all_online);
		}
		
		//修改资料请求 功能暂未实现
		if("changeInfo".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changeInfo(user));
			if(cmd.isFlag()) {
				cmd.setResult("修改信息成功");
			} else {
				cmd.setResult("修改信息失败");
			}
		}
		
		//添加好友
		if("requeste_add_friend".equals(cmd.getCmd())) {
			//检查用户是否在线
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				cmd.setResult("对方收到了您的好友请求");
			} else {
				cmd.setFlag(false);
				cmd.setResult("当前用户不在线或者改用户不存在");
			}
		}
		
		//同意添加好友请求
		if("accept_add_friend".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			cmd.setFlag(userservice.addFriend(cmd.getReceiver(), cmd.getSender()));
			if(cmd.isFlag()) {
				cmd.setResult("好友添加成功请重新登陆刷新");
			} else {
				cmd.setResult("服务器故障导致添加好友失败或者您们已经为好友");
			}
		} 
		
		//更新朋友列表
		if("updatefriendlist".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setCmd("updatefriendlist");
			//System.out.println(x);
			user = userservice.getFriendsList(user);//获取user用户的朋友列表
			if(user.getFriendsNum() == 0) {
				cmd.setFlag(false);
			} else {
				cmd.setFlag(true);
				cmd.setData(user);
			}
		}
		
		//拒绝添加好友
		if("refuse_to_add".equals(cmd.getCmd())) {
			//检查是否在线
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				cmd.setResult("您被 " + cmd.getSender() +  " 拒绝了");
			} else {
				cmd.setFlag(false);
				cmd.setResult("对方不在线不知道你拒绝了他的好友请求");
			}
		}
		
		//发送消息指令
		if("message".equals(cmd.getCmd())) {
			//检查是否在线
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				//System.out.println(cmd.getData().getClass());
				cmd.setFlag(true);
				//ImageIcon data =  (ImageIcon) cmd.getData();
				//System.out.println(data);
				//cmd.setResult("对方成功收到您的消息");
			} else {
				//System.out.println(cmd.getData().getClass());
				//System.out.println(cmd.getData());
				cmd.setFlag(false);
				//JOptionPane.showMessageDialog(null,  "对方不在线，请稍后再发！");
			 
				cmd.setResult("当前用户不在线");
			}
		}
		
		if("WordChat".equals(cmd.getCmd())) {
			cmd.setFlag(true);
		}
		
		//忘记密码指令 这里最后要讲用户的问题和答案返回
		if("forgetpwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			user = userservice.getUser(user);
			//如果用户存在
			if(user != null ) {
				cmd.setResult("查询成功");
				cmd.setData(user);
				cmd.setFlag(true);
			} else {
				cmd.setResult("用户可能不存在");
				cmd.setFlag(false);
			}
		}	
		
		if ("changepwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changePassword(user));
			System.out.println("there 111 ");
			System.out.println(user.getUsername());
			if(cmd.isFlag()) {
				cmd.setResult("修改密码成功");
			}else {
				cmd.setResult("修改密码失败");
			}
		}
		
		if("logout".equals(cmd.getCmd())) {
			//SocketList.getSocket(cmd.getSender());
			if("hand".equals(cmd.getData())){
				System.out.println(cmd.getSender());
				String sender = cmd.getSender();
				SocketList.deleteSocket(sender);
				SocketList.deleteNet(sender);
			}
		}
		
		if("update_netlist".equals(cmd.getCmd())){
			System.out.println("收到update_netlist");
			String name = cmd.getSender();
			String net = (String)cmd.getData();
			System.out.println(name+"  "+net);
			SocketList.addNet(name, net);
		}
		
		return cmd;
	}
}
