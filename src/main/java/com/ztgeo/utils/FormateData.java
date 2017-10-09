package com.ztgeo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.ztgeo.staticParams.StaticParams;

public class FormateData {

	
	//获取当前的系统时间
	public static String getNowTime(){
		Date date = new Date();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		String dateS = fm.format(date);
		return dateS;
	}
	
	public static String spitTime(String time){
		return time.substring(0, time.indexOf("."));
	}
	
	public static String getresult(String resultInt){
		String result =null;
		switch (resultInt) {
		case "0":
			result = null;
			break;
		case "-58":
			result = "发送";
			break;
		case "1":
			result = "消息结构错误";
			break;
		case "2":
			result = "命令字错误";
			break;	
		case "3":
			result = "消息序号重复";
			break;	
		case "4":
			result = "消息长度错误";
			break;	
		case "5":
			result = "资费代码错误";
			break;	
		case "6":
			result = "超过最大信息长";
			break;	
		case "7":
			result = "业务代码错误";
			break;	
		case "8":
			result = "流量控制错误";
			break;		
		default:
			result = "其他类型错误";
			break;
		}
		return result;
	}
	
	//字符串的截取 
	public static String substr(String str){
		String ywlx = str.substring(str.indexOf("办理")+2,str.indexOf("业务"));
		String slbh = str.substring(str.indexOf("受")+4,str.indexOf("已经")-1);
		String strS = "5272724330006"+"|"+ywlx+"|"+slbh;
		ywlx=null;
		slbh=null;
		return strS;
	}
	

	
}

