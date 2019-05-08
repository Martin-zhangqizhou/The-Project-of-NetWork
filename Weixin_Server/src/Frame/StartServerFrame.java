package Frame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import _Socket.Service;


public class StartServerFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JButton startServer_btn;
	private JButton endServer_btn;
	
	public StartServerFrame() {
		//创建窗口
		setLayout(new FlowLayout());
		startServer_btn = new JButton("开启服务");
		endServer_btn = new JButton("关闭服务");
		add(startServer_btn);
		add(endServer_btn);
		ImageIcon logo = new ImageIcon("image/server_image.jpg"); //左上方小图标
		setIconImage(logo.getImage());
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		startServer_btn.addActionListener(this);
		endServer_btn.addActionListener(this);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StartServerFrame startServer = new StartServerFrame();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//当点击了开启服务器按钮一定要新开启一个线程 开启动服务器 否则main线程会进入I/O阻塞~ 点击不了关闭服务器
		if(e.getSource() == startServer_btn) {
			new startServerThread().start();
			JOptionPane.showMessageDialog(null, "服务器开启成功，请连接...");	
		}
		if(e.getSource() == endServer_btn) {
			/*
			 * 这里待改进 下线之前向所有在线用户提示
			 */
			System.exit(0);
		}
	}
}

class startServerThread extends Thread{
	@Override
	public void run() {
		
		//创建socket
		Service s = new Service();
		s.startService();
	}
}
