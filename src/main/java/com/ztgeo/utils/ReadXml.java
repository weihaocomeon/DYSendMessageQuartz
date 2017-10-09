package com.ztgeo.utils;
import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ztgeo.main.Main;
import com.ztgeo.staticParams.StaticParams;

//读取xml
public class ReadXml {
	static Logger log = Logger.getLogger(ReadXml.class);
	//!地址暂时写死 后期更改	
	public static void readXmlProperty(String path) {
		SAXReader reader = new SAXReader();
		//声明xml文档
		Document doc = null;
		//读取获得对象
		File file =new File(path+"\\setProperty.xml");
		System.out.println("文件地址:"+path+"\\setProperty.xml");
		try {
		doc = reader.read(file);
		Element root = doc.getRootElement();
		//获得所有根节点下的子节点集合
		List<Element> elements = root.elements();
		for (Element e : elements) {
			String elementName = e.getName();
			switch (elementName) {
			case "username":
					System.out.println("获得的username:"+e.getText());
					log.info("获得的username:"+e.getText());
					StaticParams.username = Crypto.crypto(e.getText()) ;
				break;
			case "password":
					System.out.println("获得的password:"+e.getText());
					log.info("获得的password:"+e.getText());
					StaticParams.password = Crypto.crypto(e.getText());
					
				break;
			case "url":
				StaticParams.url = e.getText();
				break;
			case "mysqlUrl":
				StaticParams.mysqlUrl = e.getText();
				break;
			case "APPLICATIONID":
				StaticParams.APPLICATIONID = e.getText();
				break;	
			case "receiveMailAccount":
				StaticParams.receiveMailAccount = e.getText();
				break;
			case "sendMailPeople":
				StaticParams.sendMailPeople = e.getText();
				break;
			case "receiveMailPeople":
				StaticParams.receiveMailPeople = e.getText();
				break;
			case "emailTitle":
				StaticParams.emailTitle = e.getText();
				break;
			case "QuartzTime":	
				StaticParams.QuartzTime = e.getText();
				break;
			default:
				break;
		}
		}
		
		
		} catch (DocumentException e) {
			//抛出异常
			log.error("没有找到指定目录...路径:"+file.getAbsolutePath());
			Main.sbError.append(e.getLocalizedMessage());
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		System.out.println(("xml获取得到的参数:"
				+"\n"+StaticParams.url
				+"\n"+StaticParams.username
				+"\n"+StaticParams.password
				+"\n"+StaticParams.mysqlUrl
				+"\n"+StaticParams.APPLICATIONID
				+"\n"+StaticParams.receiveMailAccount
				+"\n"+StaticParams.sendMailPeople
				+"\n"+StaticParams.receiveMailPeople
				+"\n"+StaticParams.emailTitle
				+"\n"+StaticParams.QuartzTime
				));
		
		log.info(("xml获取得到的参数:"
				+"\n"+StaticParams.url
				+"\n"+StaticParams.username
				+"\n"+StaticParams.password
				+"\n"+StaticParams.mysqlUrl
				+"\n"+StaticParams.APPLICATIONID
				+"\n"+StaticParams.receiveMailAccount
				+"\n"+StaticParams.sendMailPeople
				+"\n"+StaticParams.receiveMailPeople
				+"\n"+StaticParams.emailTitle
				+"\n"+StaticParams.QuartzTime
				));
		
	}
	
}
