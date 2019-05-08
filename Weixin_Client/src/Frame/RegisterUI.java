package Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

import Entity.User;
import UserSocket.ChatTread;
import UserSocket.Client;
import _Util.CommandTranser;

 
public class RegisterUI extends JFrame implements ActionListener, FocusListener {
	private static final long serialVersionUID = 1L;
	private JLabel upper_N, user_name_txt, user_pwd_txt, user_ques_txt, user_ans_txt; //显示文字用
	private JButton register_button_S;
	private JTextField user_name, user_pwd, user_ques, user_ans; //在这里面获取用户输入
	private JPanel tmp_South, center_Center;
	private MainFrame mainFrame; //用于关闭登录页面 如果注册成功则将刚开始的注册页面关闭
	//private Client client;
	
	
	
	public RegisterUI(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		
		//初始化界面
		init();
		
		//合成整体
		add(center_Center, BorderLayout.CENTER);
		add(upper_N, BorderLayout.NORTH);
		add(tmp_South, BorderLayout.SOUTH);
		
		//位置、页面大小设置
		setSize(250, 400);
		setLocation(550, 300);
		ImageIcon logo = new ImageIcon("image/register_image.png"); //左上方小图标
		setIconImage(logo.getImage());
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	public void init() {
		
		//上方图片部分构造 上 即为 北
		ImageIcon upper_image = new ImageIcon("image/register_background_image.jpg");
		upper_image.setImage(upper_image.getImage().getScaledInstance(120, 120,
				Image.SCALE_DEFAULT));
		upper_N = new JLabel(upper_image);
		
		//下方注册图片构造 下 即为 南
		tmp_South = new JPanel(); //作为一个“壳”来装确认注册按钮
		register_button_S = new JButton();
		ImageIcon conform_register_image = new ImageIcon("image/conform_register_image.png");
		conform_register_image.setImage(conform_register_image.getImage().getScaledInstance(220, 40, Image.SCALE_DEFAULT));
		register_button_S.setIcon(conform_register_image);
		// 不绘制边框
		register_button_S.setBorderPainted(false);
		// 设置边框为空
		register_button_S.setBorder(null);
		register_button_S.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		register_button_S.addActionListener(this); //产生事件响应用户行为
		tmp_South.add(register_button_S);
		
		//中间部分内容
		center_Center = new JPanel();
		user_name_txt = new JLabel("用户账号", JLabel.CENTER);
		user_pwd_txt = new JLabel("用户密码", JLabel.CENTER);
		user_ques_txt = new JLabel("提示问题", JLabel.CENTER);
		user_ans_txt = new JLabel("问题答案", JLabel.CENTER);
		user_name = new JTextField();
		user_pwd = new JTextField();
		user_ques = new JTextField();
		user_ans = new JTextField();
		user_name.addFocusListener(this);
		user_pwd.addFocusListener(this);
		user_ans.addFocusListener(this);
		user_ques.addFocusListener(this);
		center_Center.setLayout(new GridLayout(4, 2));
		center_Center.add(user_name_txt);
		center_Center.add(user_name);
		center_Center.add(user_pwd_txt);
		center_Center.add(user_pwd);
		center_Center.add(user_ques_txt);
		center_Center.add(user_ques);
		center_Center.add(user_ans_txt);
		center_Center.add(user_ans);
		
	}
	public void actionPerformed(ActionEvent e) {
		/*
		 * 如果点击了登录按钮 首先判断帐号或者密码是否为空 然后封装为CommandTranser对象 向服务器发送数据 服务器通过与数据库的比对
		 * 来验证帐号密码
		 */
		if (e.getSource() == register_button_S) {
			String username = user_name.getText().trim();
			String password =  user_pwd.getText().trim();
			String userques = user_ques.getText().trim();
			String userans = user_ans.getText().trim();
			if ("".equals(username) || username == null) {
				JOptionPane.showMessageDialog(null, "请输入帐号！！");
				return;
			}
			if ("".equals(password) || password == null) {
				JOptionPane.showMessageDialog(null, "请输入密码！！");
				return;
			}
			
			if ("".equals(userques) || userques == null) {
				JOptionPane.showMessageDialog(null, "请输入问题！！");
				return;
			}
			if ("".equals(userans) || userans == null) {
				JOptionPane.showMessageDialog(null, "请输入答案！！");
				return;
			}
			
			User user = new User(username, password);
			user.setUserQuestion(userques);
			user.setUserAnswer(userans);
			
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("register");
			cmd.setData(user);
			cmd.setReceiver(username);
			cmd.setSender(username);
			
			// 实例化客户端 并且发送数据 这个client客户端 直到进程死亡 否则一直存在
			Client client = new Client(); //创建唯一的客户端（用于接受服务器发来的消息， socket接口）， 
			client.sendData(cmd); //发送数据
			cmd = client.getData(); //接受反馈的消息
			
			if(cmd != null) {
				if(cmd.isFlag()) {
					
					this.dispose(); //关闭注册页面
					//mainFrame.dispose(); //关闭MainFrame页面
					/*
					 * 可以改进登录窗口弹出后 一段时间后自动关闭 见https://blog.csdn.net/qq_24448899/article/details/75731529
					 */
					System.out.println("注册成功，请登录！");
					JOptionPane.showMessageDialog(null,  "注册成功，请登录！");
					/*
					user = (User)cmd.getData(); 
					System.out.println(user.getUsername() + user.getUserpwd());
					FriendsUI friendsUI;
					try {
						friendsUI = new FriendsUI(user, client);
						ChatTread thread = new ChatTread(client, user, friendsUI); //这里传client为了收消息， 整个客户端用一个 ChatTread，一个client
						thread.start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} //将user的全部信息传到FriendsUI中，并将唯一与服务器交流的接口传到FriendUI中 这里传client仅为了发送消息
					*/
				}else {
					/*
					 * 这里this和null有什么区别?
					 */
					JOptionPane.showMessageDialog(this, cmd.getResult());
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
	    	user_ques.setForeground(Color.BLACK);
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
	    	user_ques.setForeground(Color.gray);
	    }
	    	
	    //处理答案输入框
	    if(e.getSource() == user_ans){
	    	user_ans.setForeground(Color.gray);
	    }
	}

}

