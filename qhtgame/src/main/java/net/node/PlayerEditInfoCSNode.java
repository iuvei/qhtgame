package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class PlayerEditInfoCSNode {
	private int id;
	private String nickname;
	private String qq;
	private String weixin;
	private String telephone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Map<String, Object> getParameter() {
		if (	id<=0 || 
				nickname==null || nickname.length()==0 || nickname.length()>31 ||
				qq==null || qq.length()==0 || qq.length()>15 ||
				weixin==null || weixin.length()==0 || weixin.length()>31 ||
				telephone==null || telephone.length()==0 || telephone.length()>15 )
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("nickname", nickname);
		parameter.put("qq", qq);
		parameter.put("weixin", weixin);
		parameter.put("telephone", telephone);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}
