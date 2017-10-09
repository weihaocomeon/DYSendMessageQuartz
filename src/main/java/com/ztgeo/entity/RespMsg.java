package com.ztgeo.entity;

public class RespMsg {
	private String sismsid;//主键(orcle的mysqlid)
	private String senttime;//发送时间
	private String sentresult;//发送结果
	public RespMsg() {
	}
	public RespMsg(String sismsid, String senttime, String sentresult) {
		super();
		this.sismsid = sismsid;
		this.senttime = senttime;
		this.sentresult = sentresult;
	}
	/**
	 * @return sismsid
	 */
	public String getSismsid() {
		return sismsid;
	}
	/**
	 * @param sismsid 要设置的 sismsid
	 */
	public void setSismsid(String sismsid) {
		this.sismsid = sismsid;
	}
	/**
	 * @return senttime
	 */
	public String getSenttime() {
		return senttime;
	}
	/**
	 * @param senttime 要设置的 senttime
	 */
	public void setSenttime(String senttime) {
		this.senttime = senttime;
	}
	/**
	 * @return sentresult
	 */
	public String getSentresult() {
		return sentresult;
	}
	/**
	 * @param sentresult 要设置的 sentresult
	 */
	public void setSentresult(String sentresult) {
		this.sentresult = sentresult;
	}
	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RespMsg [sismsid=" + sismsid + ", senttime=" + senttime + ", sentresult=" + sentresult + "]";
	}
	
	
	
	
}
