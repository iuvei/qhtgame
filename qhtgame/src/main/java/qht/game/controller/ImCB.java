package qht.game.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.im.node.ImCBasm;
import com.im.node.ImCBasmText;
import com.node.ErrorCode;
import com.pk10.logic.Pk10OprMgr;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.HttpsUtil;

@Controller
public class ImCB {
	
	private static final String STR_RESPONE = "{\"ErrorInfo\":\"\",\"ErrorCode\":0,\"ActionStatus\":\"OK\"}";
	
	@RequestMapping(value="imcallback.do",method=RequestMethod.POST)
	@ResponseBody
	public String imCallback(HttpServletRequest request) {
		String SdkAppid = request.getParameter("SdkAppid");
		String CallbackCommand = request.getParameter("CallbackCommand");
		if (	SdkAppid==null || SdkAppid.length()==0 ||
				CallbackCommand==null || CallbackCommand.length()==0	)
			return STR_RESPONE;
		if (!SdkAppid.equals(TlsSig.SDK_APP_ID))
			return STR_RESPONE;
		String strBody = HttpsUtil.getPostData(request);
		if (strBody==null)
			return STR_RESPONE;
		try {
			switch (CallbackCommand) {
			case "Group.CallbackAfterSendMsg":
				afterSendMsg(strBody);
				break;
			default:
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return STR_RESPONE;
	}

	private static void afterSendMsg(String strBody) {
		ImCBasm data = new Gson().fromJson(strBody, ImCBasm.class);
		String From_Account = data.getFrom_Account();
		String GroupId = data.getGroupId();
		if (From_Account==null || GroupId==null)
			return;
		
		if (From_Account.equals(TlsSig.ADMIN_IDENTIFIER))	//如果是admin本身，不处理
			return;
		
		List<ImCBasmText> list = data.getMsgBody();
		if (list.size()!=1)
			return;
		ImCBasmText imText = list.get(0);
		if (imText==null)
			return;
		Map<String, String> _map = imText.getMsgContent();
		if (_map==null)
			return;
		String text = _map.get("Text");
		if (text==null)
			return;
		
		ErrorCode mycode = null;
		if (GroupId.equals(TlsSig.PK10_GROUP_ID))
			mycode = Pk10OprMgr.deal(From_Account,TlsSig.ADMIN_IDENTIFIER, GroupId, text);
		if (mycode!=null)
			ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, GroupId, mycode.getDesc(), false);
		//ImOpr.sendGroupMsgSystem(GroupId, From_Account, respone);
	}

	
}
