package com.ztgeo.sendMessage;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ztgeo.dao.Dao;
import com.ztgeo.entity.RespMsg;
import com.ztgeo.entity.TelS;
import com.ztgeo.main.Main;
import com.ztgeo.staticParams.StaticParams;
import com.ztgeo.utils.FormateData;
import com.ztgeo.utils.ReadXml;



//发短息的类
public class SendMessage {
	Logger log = Logger.getLogger(SendMessage.class);
	//发送数据的组织类
	public void sendMsg(List<TelS> tels) {
		if (tels.size()==0) {
			System.out.println("当前无最新消息....");
		}
		//追条插入信息
		for (TelS tel : tels) {
			System.out.println("当前短信信息的ID为:"+tel.getId());
			//将结果集转存到mysql
			sendMessage(tel);
			//释放资源
			
		}
		//查询库中未同步数据,进行获取反馈 
		synData();
		
	}
	
	private void synData() {
		System.out.println("同步反馈数据开始执行-----");
			Dao.getConnO(StaticParams.url, StaticParams.username , StaticParams.password);
		String findsyn = "select mysqlid from SMS_DETAILINFO where status=0";
			//将结果集进行保存成list
			//将list进行解析后连接mysql库 并拿到相关结果 
			ResultSet set =Dao.getData(findsyn);
			findsyn=null;
			List<String> sets =resulttoList(set);
			if(sets.size()>0&&sets.get(0)!=null){
				//获取相应更新数据
				List<RespMsg> list = getResult(sets);
				//将集合遍历 去更新每条数据回执  
				updataO(list);
			}else{
				System.out.println("---未发现需要同步的数据!");
				log.info("---未发现需要同步的数据!");
			}
			
			sets=null;
			
		
	}

	private void updataO(List<RespMsg> list) {
			for (RespMsg respMsg : list) {
				String sql =  "update  sms_detailinfo t set(t.status,t.remarks,t.sendcount,t.sendtime,t.errormsg)=\n" +
			            "(select 1,?,(t.sendcount+1), to_date(?,'YYYY-MM-DD HH24:MI:SS'),? from sms_detailinfo t where t.mysqlid =?)\n" + 
									" where t.mysqlid=?";
				String[] params = new String[5];
				params[0]= "0".equals(respMsg.getSentresult())?"发送成功":"发送失败"; //状态
				params[1]= FormateData.spitTime(respMsg.getSenttime()); //时间
				params[2]= FormateData.getresult(respMsg.getSentresult());//错误提醒
				params[3]= respMsg.getSismsid();
				params[4]= respMsg.getSismsid();
				Dao.getConnO(StaticParams.url, StaticParams.username , StaticParams.password);
				try {
					Dao.doExecuteUpdate(sql,params);
					System.out.println("mysqlID为:"+respMsg.getSismsid()+"的短信信息请求状态已更新");
					log.info("mysqlID为:"+respMsg.getSismsid()+"的短信信息请求状态已更新");
					//数组的释放
					
				} catch (SQLException e) {
					System.out.println("同步数据时更新操作时发生异常!!!");
					System.out.println("mysqlID为:"+respMsg.getSismsid()+"的短信信息请求状态失败");
					Main.sbError.append("mysqlID为:"+respMsg.getSismsid()+"的短信信息请求状态失败");
					log.error("执行更新操作时发生异常!!!"+e);
					e.printStackTrace();
				}finally {
					//关闭资源
					Dao.closeResource();
					Dao.closeConn();
					params=null;
					sql=null;
				}
			}
	}

	private List<RespMsg> getResult(List<String> resulttoList) {
		//拼接字符串
		List<RespMsg> respMsgs = new ArrayList<>();
			ResultSet set;
			StringBuffer sql = new StringBuffer();
			
			sql.append("select sismsid,senttime,sentresult from sms_sent where sismsid in(");
			for (int i = 0; i < resulttoList.size(); i++) {
				if(i<(resulttoList.size()-1)){
					sql.append("'"+resulttoList.get(i)+"',");
				}else{
					sql.append("'"+resulttoList.get(i)+"'");
				}
			}
			sql.append(")");
			System.out.println(sql.toString());
			Dao.getConnM(StaticParams.mysqlUrl);
			try {
				 set =Dao.getData(sql.toString());
				while(set.next()){
					respMsgs.add(new RespMsg(set.getString("sismsid"), set.getString("senttime"),set.getString("sentresult")));
					System.out.println(set.getString("sismsid")+set.getString("senttime")+set.getString("sentresult"));
				}
				//关闭资源  
				Dao.closeResource();
				Dao.closeConn();
				//更新原有的信息
				
			} catch (SQLException e) {
				System.out.println("获取mysql数据出现问题");
				Main.sbError.append("获取mysql数据出现问题");
				log.error("获取mysql数据出现问题",e);
				e.printStackTrace();
			}finally {
				//关闭资源
				Dao.closeResource();
				Dao.closeConn();
				sql=null;
				set=null;
			}
		
		return respMsgs;
	}

	@Test
	public void test(){

		File directory = new File("xml");//设定为当前文件夹 
		String path="";
		
		
	    path = directory.getAbsolutePath();//获取标准的路径 
	    //开发环境 该目录可用
	    ReadXml.readXmlProperty(path);
		new SendMessage().synData();
	}
	
	private List<String> resulttoList(ResultSet data) {
		List<String> list = new ArrayList<>();
		try {
			while(data.next()){
				list.add(data.getString("mysqlid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Dao.closeConn();
			Dao.closeResource();
			data=null;
		}
		
		return list;
	}

	private void sendMessage(TelS tel) {
		//获取数据库连接  
		String id =UUID.randomUUID().toString();
		Dao.getConnM(StaticParams.mysqlUrl);
		//存入mysql
		String sql ="INSERT INTO `sms_outbox` (\n" +
				"	`SISMSID`,\n" +
				"	`SENDMETHOD`,\n" +
				"	`DESTADDR`,\n" +
				"	`REQDELIVERYREPORT`,\n" +
				"	`APPLICATIONID`,\n" +
				"	`REQUESTTIME`,\n" +
				"	`MESSAGECONTENT`,\n" +
				"   `MSGFMT`)\n" +
				"VALUES\n" +
				"	(\n" +
				"		?,\n" +
				"		0,\n" +
				"		?,\n" +
				"		'1',\n" +
				"		?,\n" +
				"		NOW(),\n" +
				"		?,\n" +
				"		15)";
		Object[] params = new Object[4];
		params[0]=id;
		params[1]=tel.getTel();
		params[2]=StaticParams.APPLICATIONID;
		params[3]=FormateData.substr(tel.getContent());
		System.out.println("分割后的数据是:"+FormateData.substr(tel.getContent()));
		//执行当前语句  
		try {
			Dao.doExecuteUpdate(sql,params);
			System.out.println("ID为:"+tel.getId()+"的短信推送成功");
			log.info("ID为:"+tel.getId()+"的短信推送成功");
			//关闭资源  
			Dao.closeResource();
			Dao.closeConn();
			//更新原有的信息
			updateMsg(tel,"请求成功",id);
			
		} catch (SQLException e) {
			System.out.println("推送短信至mysql失败!!!ID为:"+tel.getId());
			Main.sbError.append("推送短信至mysql失败!!!ID为:"+tel.getId());
			log.error("推送短信至mysql失败!!!ID为:"+tel.getId(),e);
			updateMsg(tel,"请求失败",id);
			e.printStackTrace();
		}finally {
			//关闭资源
			Dao.closeResource();
			Dao.closeConn();
			params=null;
			id=null;
		}
	}

	private void updateMsg(TelS tel,String content, String id) {
		//连接数据库
		Dao.getConnO(StaticParams.url, StaticParams.username , StaticParams.password);
		//组织查询语句
		Object[] params = new Object[3];
		String sql = "update sms_detailinfo t set t.remarks =\n" + 
					"?,t.mysqlid=? \n" + 
					"where t.id = ?";
		params[0]=content;
		params[1]=id;
		params[2]=tel.getId();
	
		//更新表
		try {
			Dao.doExecuteUpdate(sql,params);
			System.out.println("ID为:"+tel.getId()+"的短信信息请求状态已更新");
			log.info("ID为:"+tel.getId()+"的短信信息请求状态已更新");
			//数组的释放
			
		} catch (SQLException e) {
			System.out.println("执行更新操作时发生异常!!!");
			System.out.println("ID为:"+tel.getId()+"的短信信息请求状态更新失败!!");
			Main.sbError.append("执行更新操作时发生异常!!!ID为:"+tel.getId()+"的短信信息请求状态更新失败!!");
			log.error("执行更新操作时发生异常!!!");
			log.error("ID为:"+tel.getId()+"的短信信息请求状态更新失败!!");
			e.printStackTrace();
		}finally {
			//关闭资源
			Dao.closeResource();
			Dao.closeConn();
			params=null;
		}
	}

	
	
	
	//获得需要发送信息的list集合
	public List<TelS> getSendData() {
		//连接数据库
		Dao.getConnO(StaticParams.url, StaticParams.username , StaticParams.password);
		//组织查询语句
		String sql = "select T.PHONENUMBER,T.CONTENT,T.ID from sms_detailinfo T where remarks  ='服务开始准备发送短信'";
		//进行查询后取值
		ResultSet set = Dao.getData(sql);
		//放入list
		List<TelS> tels = getListBySet(set);
		//关闭资源
		Dao.closeResource();
		Dao.closeConn();
		return tels;
	}

	private List<TelS> getListBySet(ResultSet set) {
		List<TelS> tels = new ArrayList<>();
		try {
			while(set.next()){
				TelS tel = new TelS(set.getString("PHONENUMBER"),set.getString("ID"),set.getString("CONTENT"));
				tels.add(tel);
			}
		} catch (SQLException e) {
			Main.sbError.append("处理获取未发短信时的set集合时发生问题!!!");
			log.error("处理set集合时发生问题!!!",e);
			System.out.println("处理set集合时发生问题!!!");
			e.printStackTrace();
		}
		return tels;
	}

	
}
