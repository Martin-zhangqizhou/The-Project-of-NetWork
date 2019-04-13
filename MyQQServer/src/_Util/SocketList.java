package _Util;

import java.net.Socket;
import java.util.HashMap;

import Entity.SocketEntity;


public class SocketList {
	private static HashMap<String, Socket> map = new HashMap<String, Socket>();
	public static void addSocket(SocketEntity socketEntity) {
		map.put(socketEntity.getName(), socketEntity.getSocket());	
	}
	
	//通过昵称返回socket 类比socklist在客户端创建 ChatUIList
	public static Socket getSocket(String name) {
		return map.get(name);
	}
	
	public static HashMap<String, Socket> getMap(){
		return map;
	}
	
	public static void deleteSocket(String name) {
		if(map.get(name) != null) {
			map.remove(name);
		}
		return;
	}
}
