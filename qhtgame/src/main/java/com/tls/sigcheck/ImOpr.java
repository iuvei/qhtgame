package com.tls.sigcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.node.ImAddFriendClient;
import com.im.node.ImAddFriendClientNode;
import com.im.node.ImCBasmText;
import com.im.node.ImGroupMemberNode2;
import com.im.node.ImGroupMemberResult;
import com.im.node.ImGroupMsgSystemClient;
import com.node.MapData;
import com.tls.sigcheck.TlsSig;
import com.util.RandUtil;

public class ImOpr {
	
	public static boolean init() {
		if (!ImOpr.accountImport(TlsSig.APP_GROUP_ADMIN))
			return false;
		if (!ImOpr.accountImport(TlsSig.APP_GROUP_CUSTOM))
			return false;
		if (!ImOpr.accountImport(TlsSig.PK10_GROUP_ADMIN))
			return false;
		if (!ImOpr.accountImport(TlsSig.PK10_GROUP_CUSTOM))
			return false;
		/*if (*/ImOpr.createGroup(TlsSig.APP_GROUP_ID,TlsSig.APP_GROUP_ID,"Public",TlsSig.APP_GROUP_ADMIN);/*==null)*/
			/*return false;*/
		/*if (*/ImOpr.createGroup(TlsSig.PK10_GROUP_ID,TlsSig.PK10_GROUP_ID,"Public",TlsSig.PK10_GROUP_ADMIN);/*==null)*/
			/*return false;*/
		return true;
	}

/*	
{
	   "Identifier":"test",
	   "Nick":"test",
	   "FaceUrl":"http://www.qq.com"
}	

{
   "ActionStatus":"OK",
   "ErrorInfo":"",
   "ErrorCode":0
}
*/	
	/*
	 * 账号导入
	 */
	public static boolean accountImport(String identifier) {
		MapData mapData = new MapData();
		mapData.put("Identifier", identifier);
		String data = new Gson().toJson(mapData);
		
		String result = TlsSig.doPost(TlsSig.account_import,data);
		if (result==null)
			return false;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return false;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return false;
		if (!actionStatus.equals("OK"))
			return false;
		
		return true;
	}
/*
	"Name": "TestGroup", // 群名称（必填）
    "Type": "Public", // 群组类型：Private/Public/ChatRoom(不支持AVChatRoom)（必填）
    "MemberList": [  // 初始群成员列表，最多500个（选填）
         {
            "Member_Account": "bob", // 成员（必填）
            "Role": "Admin" // 赋予该成员的身份，目前备选项只有Admin（选填）
         }, 
         {
            "Member_Account": "peter"
         }
     ]
 */
/*
{
    "ActionStatus": "OK", 
    "ErrorInfo": "", 
    "ErrorCode": 0, 
    "GroupId": "MyFirstGroup" 
}
*/
	/*
	 * 创建一个群，并添加管理员
	 */
	public static String createGroup(String groupID, String groupName,String type, String admin) {
		MapData mapData = new MapData();
		mapData.put("Owner_Account", admin);
		mapData.put("Name", groupID);
		mapData.put("Type", type);
		mapData.put("GroupId", groupName);
		String data = new Gson().toJson(mapData);

		String result = TlsSig.doPost(TlsSig.create_group,data);
		if (result==null)
			return null;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return null;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return null;
		if (!actionStatus.equals("OK"))
			return null;

		return (String)map.get("GroupId");
	}

/*	
	{
	    "GroupId": "@TGS#2J4SZEAEL",   // 要操作的群组（必填）          
	    "MemberList": [  // 一次最多添加500个成员       
	    {          
	        "Member_Account": "tommy"  // 要添加的群成员ID（必填）        
	    },        
	    {           
	        "Member_Account": "jared"       
	    }]
	}
*/
	/*
	 * 玩家加群
	 */
	public static boolean addGroupMember(String groupID,String memberAccount) {
		List<ImGroupMemberNode2> list = new ArrayList<ImGroupMemberNode2>();
		ImGroupMemberNode2 member = new ImGroupMemberNode2();
		member.setMember_Account(memberAccount);
		list.add(member);
		//String strList = new Gson().toJson(list);
		MapData mapData = new MapData();
		mapData.put("GroupId", groupID);
		mapData.put("MemberList", list);
		String data = new Gson().toJson(mapData);
		
		String result = TlsSig.doPost(TlsSig.add_group_member,data);
		if (result==null)
			return false;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return false;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return false;
		if (!actionStatus.equals("OK"))
			return false;

		Object strList = map.get("MemberList");
		if (strList==null)
			return false;
		List<ImGroupMemberResult> resultList = new Gson().fromJson(strList.toString(),new TypeToken<List<ImGroupMemberResult>>() {}.getType());
		
		if (resultList.size()!=1)
			return false;
		ImGroupMemberResult node = resultList.get(0);
		if (node.getResult()!=1 && node.getResult()!=2)
			return false;
		
		return true;
	}
	
	/*
	 * 发送群消息
	 */
	public static boolean sendMsg(String sendID,String groupID,String text,boolean isCB) {
		MapData mapData = new MapData();
		mapData.put("GroupId", groupID);
		mapData.put("From_Account", sendID);
		mapData.put("Random", RandUtil.randInt(1000000,9999999));
		if (!isCB) {	//禁止回调控制选项
			List<String> list = new ArrayList<String>();
			list.add("ForbidBeforeSendMsgCallback");
			list.add("ForbidAfterSendMsgCallback");
			mapData.put("ForbidCallbackControl", list);
		}
		
		Map<String,String> _map = new HashMap<String,String>();
		_map.put("Text", text);
		ImCBasmText member = new ImCBasmText();
		member.setMsgType("TIMTextElem");
		member.setMsgContent(_map);
		
		List<ImCBasmText> list = new ArrayList<ImCBasmText>();
		list.add(member);
		
		mapData.put("MsgBody", list);
		String data = new Gson().toJson(mapData);

		String result = TlsSig.doPost(TlsSig.send_group_msg,data);
		if (result==null)
			return false;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return false;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return false;
		if (!actionStatus.equals("OK"))
			return false;
		
		return true;
	}
	

	
	/*
	 * 添加好友
	 */
	public static boolean addFriend(String fromAccount, String friendAccount) {
		ImAddFriendClient sendMsg = new ImAddFriendClient();
		sendMsg.setFrom_Account(fromAccount);
		List<ImAddFriendClientNode> list = new ArrayList<ImAddFriendClientNode>();
		ImAddFriendClientNode node = new ImAddFriendClientNode();
		node.setTo_Account(friendAccount);
		node.setAddSource("AddSource_Type_friend");
		list.add(node);
		sendMsg.setAddFriendItem(list);
		String data = new Gson().toJson(sendMsg);
		
		String result = TlsSig.doPost(TlsSig.friend_add,data);
		if (result==null)
			return false;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return false;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return false;
		if (!actionStatus.equals("OK"))
			return false;
		
		return true;
	}

	
	/*
	 * 摘取群消息
	 */
	public static void getGroupMsg(String groupID) {
		MapData mapData = new MapData();
		mapData.put("GroupId", groupID);
		mapData.put("ReqMsgNumber", 20);
		String data = new Gson().toJson(mapData);
		
		String result = TlsSig.doPost(TlsSig.group_msg_get_simple,data);
		if (result==null)
			return;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return;
	}
	
	/*
	 * 在群组中发送系统通知
	 */
	public static boolean sendGroupMsgSystem(String groupID, String toAccount, String content) {
		List<String> list = new ArrayList<String>();
		if (toAccount!=null)
			list.add(toAccount);
		
		ImGroupMsgSystemClient sendMsg = new ImGroupMsgSystemClient();
		sendMsg.setGroupId(groupID);
		//sendMsg.setToMembers_Account(list);
		sendMsg.setContent(content);
		
		String data = new Gson().toJson(sendMsg);
		
		String result = TlsSig.doPost(TlsSig.group_msg_system,data);
		if (result==null)
			return false;
		
		MapData map = new Gson().fromJson(result, MapData.class);
		if (map==null)
			return false;
		String actionStatus = (String)map.get("ActionStatus");
		if (actionStatus==null)
			return false;
		if (!actionStatus.equals("OK"))
			return false;
		
		return true;
	}
	
}
