package Frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Entity.User;
import UserSocket.ChatTread;
import UserSocket.Client;
import _Util.CommandTranser;

/**
*  忘记密码界面
*/
public class ForgetUI extends JFrame implements ActionListener, FocusListener{
	private static final long serialVersionUID = 1L;
	private JLabel upper_N, user_name_txt, user_pwd_txt, user_ques_txt, user_ans_txt, user_ques; //显示文字用
	private JButton forget_button_S, submit_button;
	private JTextField user_name, user_pwd, user_ans; //在这里面获取用户输入
	//private JPanel tmp_South, center_Center;
	private MainFrame mainFrame; //用于关闭登录页面 如果修改密码成功则将刚开始的注册页面关闭
	private User user;
	private Client client; //用于查询数据库中是否有该用户
	
	public ForgetUI(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		//页面构造
		init();
		//合成整体
		add(upper_N);
		add(user_name_txt);
		add(user_name);
		add(submit_button);
		add(user_ques_txt);
		add(user_ques);
		add(user_ans_txt);
		add(user_ans);
		add(user_pwd_txt);
		add(user_pwd);
		add(forget_button_S);
		
		//位置、页面大小设置
		setSize(270, 430);
		setLocation(550, 300);
		setLayout(null); //手工布局 与其他页面不同 
		ImageIcon logo = new ImageIcon("image/register_image.jpg"); //左上方小图标
		setIconImage(logo.getImage());
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setVisible(true);		
	}
	
	public void init() {
		//上方图片部分构造 上 即为 北
		ImageIcon upper_image = new ImageIcon("image/forget_background_image.png");
		upper_image.setImage(upper_image.getImage().getScaledInstance(270, 170,
				Image.SCALE_DEFAULT));
		upper_N = new JLabel(upper_image);
		upper_N.setLocation(0,0); //确定位置
		upper_N.setSize(270, 170); //设置大小
		
		
		//中间部分内容 绝对位置
		user_name_txt = new JLabel("用户账号", JLabel.CENTER);
		user_name_txt.setSize(60, 20);
		user_name_txt.setLocation(10, 185);
		
		submit_button = new JButton();
		submit_button.setText("查询");
		submit_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		submit_button.setSize(60, 20);
		submit_button.setLocation(190, 185);
		submit_button.addActionListener(this); //产生事件响应用户行为
		
		user_ques_txt = new JLabel("提示问题", JLabel.CENTER);
		user_ques_txt.setSize(60, 20);
		user_ques_txt.setLocation(10, 220);
		
		user_ans_txt = new JLabel("问题答案", JLabel.CENTER);
		user_ans_txt.setSize(60, 20);
		user_ans_txt.setLocation(10, 255);
		
		user_pwd_txt = new JLabel("重置密码", JLabel.CENTER);
		user_pwd_txt.setSize(60, 20);
		user_pwd_txt.setLocation(10, 290);
		
		user_name = new JTextField();
		user_name.setSize(100, 30);
		user_name.setLocation(80, 185);
		
		user_ques = new JLabel("点击查询后显示", JLabel.CENTER);
		user_ques.setSize(100, 30);
		user_ques.setLocation(80, 220);
		
		user_ans = new JTextField();
		user_ans.setSize(100, 30);
		user_ans.setLocation(80, 255);
		
		user_pwd = new JTextField();
		user_pwd.setSize(100, 30);
		user_pwd.setLocation(80, 290);
		
			
		user_name.addFocusListener(this);
		user_pwd.addFocusListener(this);
		user_ans.addFocusListener(this);
		
		
		//下方注册图片构造 下 即为 南
		forget_button_S = new JButton();
		ImageIcon conform_register_image = new ImageIcon("image/conform_forget_image.png");
		conform_register_image.setImage(conform_register_image.getImage().getScaledInstance(220, 32, Image.SCALE_DEFAULT));
		forget_button_S.setIcon(conform_register_image);
		forget_button_S.setBorderPainted(false);
		forget_button_S.setBorder(null);
		forget_button_S.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		forget_button_S.setSize(220, 32);
		forget_button_S.setLocation(15, 330);
		forget_button_S.addActionListener(this); //产生事件响应用户行为
			
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == submit_button) {
			String username = user_name.getText().trim();
			if("".equals(username) || username == null) {
				JOptionPane.showMessageDialog(null, "请输入帐号！！");
				return;
			} else {
				user = new User();
				user.setUsername(username);
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("forgetpwd");
				cmd.setReceiver(username);
				cmd.setSender(username);
				cmd.setData(user);
				client = new Client();
				client.sendData(cmd);
				cmd = client.getData();
				
				if(cmd != null) {
					if(cmd.isFlag()) {
						user = (User)cmd.getData();
						user_ques.setText(user.getUserQuestion());
						//JOptionPane.showMessageDialog(null, "查询成功"); //this 跟 null很迷
					}else {
						JOptionPane.showMessageDialog(null, cmd.getResult());
						//账号输入框置空
						user_name.setText("");	
					}
				}else {
					
				}
			}
		}
		if (e.getSource() == forget_button_S) {
			if(user == null || "".equals(user.getUsername()) || user.getUserQuestion() == null || "".equals(user.getUserQuestion())) {
				JOptionPane.showMessageDialog(null, "请输入正确账号并点击查询"); 
				return;
			} 
			
			String newpwd = user_pwd.getText();
			if(newpwd == null || "".equals(newpwd)) {
				JOptionPane.showMessageDialog(null, "请输入新密码"); 
				return;
			}
			
			if(user.getUserAnswer() == null || "".equals(user.getUserAnswer())) {
				JOptionPane.showMessageDialog(null, "您的账户未设置问题不能找回"); 
				return;
			}
			
			String userans = user_ans.getText();
			
			if(userans == null || "".equals(userans)) {
				JOptionPane.showMessageDialog(null, "请输入答案");
				
				return;
			}
			
			String username = user_name.getText().trim();
			//这里判断username是必要的
			if(!userans.equals(user.getUserAnswer()) || username == null || !username.equals(user.getUsername()) ) {
				JOptionPane.showMessageDialog(null, "答案错误"); 
				return;
			}
			
			//验证成功， 开始修改密码
			CommandTranser cmd = new CommandTranser();
			user.setUsername(username);
			user.setUserpwd(newpwd);
			cmd.setCmd("changepwd");
			cmd.setData(user);
			cmd.setReceiver(username);
			cmd.setSender(username);
			if(client == null) {
				JOptionPane.showMessageDialog(null, "意外错误"); 
				return;
			}
			client.sendData(cmd);
			
			cmd = client.getData();
			
			if(cmd != null) {
				if(cmd.isFlag()) {
					JOptionPane.showMessageDialog(null,  cmd.getResult());
					//登录
					cmd.setCmd("login");
					if(client == null) {
						JOptionPane.showMessageDialog(null, "意外错误"); 
						return;
					}
					client.sendData(cmd);
					cmd = client.getData();
					if(cmd != null) {
						if(cmd.isFlag()) {
							this.dispose();
							//mainFrame.dispose();
							//JOptionPane.showMessageDialog(null,  "密码修改成功，请登录");
							/*
							user = (User)cmd.getData(); 
							FriendsUI friendsUI;
							try {
								friendsUI = new FriendsUI(user, client);
								ChatTread thread = new ChatTread(client, user, friendsUI);
								thread.start();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/
						}
					}
					
				} else {
					JOptionPane.showMessageDialog(null, cmd.getResult()); 
					return;
				}
			}
			
		}
	}
	
	//鼠标的点击或移动之类的用focuslistener 这里不知道为啥没变色 （哭
	@Override
	public void focusGained(FocusEvent e) {
		//处理账号输入框
	    if(e.getSource() == user_name){
	    	user_name.setForeground(Color.BLACK);
	    }
	    	
	    //处理密码输入框
	    if(e.getSource() == user_pwd){
	    	user_pwd.setForeground(Color.BLACK);
	    }
	    
	  //处理问题输入框
	    if(e.getSource() == user_ques){
	    	//user_ques.setForeground(Color.BLACK);
	    }
	    	
	    //处理答案输入框
	    if(e.getSource() == user_ans){
	    	user_ans.setForeground(Color.BLACK);
	    }
	    
	}
	@Override
	public void focusLost(FocusEvent e) {
		//处理账号输入框
	    if(e.getSource() == user_name){
	    	user_name.setForeground(Color.gray);
	    }
	    	
	    //处理密码输入框
	    if(e.getSource() == user_pwd){
	    	user_pwd.setForeground(Color.gray);
	    }
	    
	  //处理问题输入框
	    if(e.getSource() == user_ques){
	    	//user_ques.setForeground(Color.gray);
	    }
	    	
	    //处理答案输入框
	    if(e.getSource() == user_ans){
	    	user_ans.setForeground(Color.gray);
	    }
	}

}


