package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import javax.swing.ImageIcon;

 
public class User extends Person implements Serializable{
	private static final long serialVersionUID = 1L;
	private String user_pwd;
	private String ques_to_retrieve_the_pwd;
	private String ans_to_retrieve_the_pwd;
	private int friends_num = 0;
	private int groups_num = 0;
	private ArrayList<String> friendslist;
	
	public User() {
		super();
	}
	
	public User(String username, String password) {
		super();
		this.user_name = username;
		this.user_pwd = password;
	}

	public String setUsername(String username) {
		return this.user_name = username;
	}

	public String getUsername() {
		return user_name;
	}
	
	public String getUserpwd() {
		return user_pwd;
	}

	public String setUserpwd(String password) {
		return this.user_pwd = password;
	}
	
	
	public String setUserNickname(String nickname) {
		return this.user_nickname = nickname;
	}
	
	public String getUserQuestion() {
		return ques_to_retrieve_the_pwd;
	}
	
	public String setUserQuestion(String question) {
		return this.ques_to_retrieve_the_pwd = question;
	}
	
	public String getUserAnswer() {
		return ans_to_retrieve_the_pwd;
	}
	
	public String setUserAnswer(String answer) {
		return this.ans_to_retrieve_the_pwd = answer;
	}
	

	public int setUserSex(int sex) {
		return this.user_sex = sex;
	}
	public ImageIcon setUserAvata(ImageIcon user_avata) {
		return this.user_avata = user_avata;
	}
	public int getFriendsNum() {
		return friends_num;
	}
	public void setFriendsNum(int friends_num) {
		this.friends_num = friends_num;
	}
	public void setFriendsList(ArrayList<String> friendList) {
		Collections.sort(friendList);
		this.friendslist =  friendList;
		this.friends_num = friendList.size();
	}
	
	public ArrayList<String> getFriend() {
		return friendslist;
	}
	
}