package Frame;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Entity.User;
import UserSocket.Client;
import _Util.CommandTranser;

/*
 * 改密码界面
 */
 
public class ChangePwdUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private User owner;// 当前用户
	private Client client;// 客户端
	
	private JButton submit_bt;
	
	private JLabel txt_input_pwd, txt_reinput_pwd;
	private JTextField new_pwd, re_new_pwd;
	
	private JPanel center, souTh;
	
	public ChangePwdUI(User owner, Client client) {
		
		this.owner = owner;
		this.client = client;
		
		init();
		
		add(center, BorderLayout.CENTER);
		add(souTh, BorderLayout.SOUTH);
		
		setBounds(200, 230, 360, 150); //设定大小及位置
		setResizable(false); //登录框大小固定，不允许通过拖、拉改变大小
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //设置窗口右上角的叉号，点击叉号窗口关闭 注意不能EXIT_ON_CLOSE做参数的，用它时候使用的是System.exit方法退出应用程序。故会关闭所有窗口。
		setTitle("修改密码");
		setVisible(true);
				
	}
	public void init() {
		txt_input_pwd = new JLabel();
		txt_input_pwd.setText("请输入新密码");
		
		txt_reinput_pwd = new JLabel();
		txt_reinput_pwd.setText("请重复输入");
		
		new_pwd = new JTextField();
		new_pwd.setName("new_pwd");
		
		re_new_pwd = new JTextField();
		re_new_pwd.setName("new_pwd");
		
		submit_bt = new JButton();
		submit_bt.setText("确认修改");
		submit_bt.setBorderPainted(false);
		submit_bt.setBorder(BorderFactory.createRaisedBevelBorder());
		submit_bt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
		submit_bt.addActionListener(this);
		
		center = new JPanel();
		center.setLayout(new GridLayout(2, 2, 3, 15)); 
		center.add(txt_input_pwd);
		center.add(new_pwd);
		center.add(txt_reinput_pwd);
		center.add(re_new_pwd);
		
		souTh = new JPanel();
		souTh.setLayout(new GridLayout(1, 3, 3, 15)); 
		JLabel empty_1 = new JLabel("");
		JLabel empty_2 = new JLabel("");
		souTh.add(empty_1);
		souTh.add(submit_bt);
		souTh.add(empty_2);
	}
	
	//按钮的点击事件用actionPerformed
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == submit_bt){
			String pwd = new_pwd.getText().trim();
			String re_pwd = re_new_pwd.getText().trim();
			if("".equals(pwd) || pwd == null) {
				JOptionPane.showMessageDialog(null, "请输入新密码！！");
				return;
			}
			if("".equals(re_pwd) || re_pwd == null) {
				JOptionPane.showMessageDialog(null, "请重复输入密码！！");
				return;
			}
			
			if(!pwd.equals(re_pwd)) {
				JOptionPane.showMessageDialog(null, "两次密码不一致！！");
				re_new_pwd.setText("");
				return;
			}
			
			owner.setUserpwd(re_pwd);
			CommandTranser cmd = new CommandTranser();
			
			cmd.setCmd("changepwd");
			cmd.setData(owner);
			cmd.setReceiver(owner.getUsername());
			cmd.setSender(owner.getUsername());
			
			client.sendData(cmd); //发送数据
			
			this.dispose();
		}	
	}
}
