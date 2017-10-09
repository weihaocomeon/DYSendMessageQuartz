package com.ztgeo.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.log4j.Logger;


import com.ztgeo.utils.Email;
import com.ztgeo.utils.FormateData;
import com.ztgeo.utils.Quartz;
import com.ztgeo.utils.ReadXml;
import com.ztgeo.utils.StringStr;
import com.ztgeo.utils.SystemPrintln;

public class Main extends Thread{
		//记录异常信息
		public static StringBuffer sbError = new StringBuffer();
		Logger log = Logger.getLogger(Main.class);
		JFrame jf;
		JPanel jpanel;
		JScrollPane jscrollPane;
		//JButton jb1, jb2, jb3;

		public Main() {
			UIManager.put("RootPane.setupButtonVisible", false);
			try {
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			  
			jf = new JFrame("泗阳邮件信息发送程序");
			Container contentPane = jf.getContentPane();
			contentPane.setLayout(new BorderLayout());
			StringStr.jta=new JTextArea(10,15);
			StringStr.jta.setTabSize(4);
			StringStr.jta.setFont(new Font("标楷体", Font.BOLD, 16));
			StringStr.jta.setLineWrap(true);// 激活自动换行功能
			StringStr.jta.setWrapStyleWord(true);// 激活断行不断字功能
			StringStr.jta.setBackground(Color.WHITE);	
			jscrollPane = new JScrollPane(StringStr.jta);
			jpanel = new JPanel();
			jpanel.setLayout(new GridLayout(1, 3));
			/*jb1 = new JButton("开始");
			jb2 = new JButton("结束并退出");*/
			/*jb1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jb1.setEnabled(false);
					start();
					
				}
			});*/

			/*jb2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("该程序不允许被关闭!");
					System.exit(0);
					
				}
			});
			
			jpanel.add(jb1);
			jpanel.add(jb2);*/
			contentPane.add(jscrollPane, BorderLayout.CENTER);
			contentPane.add(jpanel, BorderLayout.SOUTH);
			jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			jf.setSize(800, 600);
			jf.setLocation(400, 200);
			jf.setVisible(true);
			jf.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					/*System.exit(0);*/
					System.out.println("该程序不允许被关闭!");
				}
			});
		//
		}
		@Override
		public void run() {
				String dateS = FormateData.getNowTime();
				System.out.println("※程序启动中-----启动日期:※"+dateS+"※");
				log.info("※程序启动中-----启动日期:※"+dateS+"※");
				File directory = new File("xml");//设定为当前文件夹 
				String path="";
				
				
			    path = directory.getAbsolutePath();//获取标准的路径 
			    //开发环境 该目录可用
			    ReadXml.readXmlProperty(path);
			    
			  //主方法
				Quartz.startQuartz();
				
				}
		public static void main(String[] args) {
			new SystemPrintln();
			Main m = new Main();
			m.run();
		}		
		
	
}