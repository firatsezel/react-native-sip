package com.carusto.ReactNativePjSip;

import org.json.JSONObject;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.BuddyConfig;

public class PjSipMessage {

    private PjSipAccount account;

    private OnInstantMessageParam prm;

    private PjSipMyBuddy mBuddy = null;

    public PjSipMessage(PjSipAccount account) {
        this.account = account;
    }

    public PjSipMessage(PjSipAccount account, OnInstantMessageParam prm) {
        this.account = account;
        this.prm = prm;
    }

    public int sendMessage(String msgBody, String remoteUser) {
		if (account == null || remoteUser == null
				|| remoteUser.isEmpty() || msgBody == null || msgBody.isEmpty()) {
			return -1;
		}
		if (mBuddy != null) {
			for (int i = 0; i < account.buddyList.size(); i++) {
				account.delBuddy(i);
			}
		}
		try {
			BuddyConfig buddyConfig = new BuddyConfig();
			buddyConfig.setUri(remoteUser); //<sip:mobile2@sip.kentkart.com>
			mBuddy = account.addBuddy(buddyConfig);
			SendInstantMessageParam messageParam = new SendInstantMessageParam();
			messageParam.setContent(msgBody);
			mBuddy.sendInstantMessage(messageParam);
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
		return 0;
	}

    public OnInstantMessageParam getParam() {
        return prm;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        try {
            // -----
            json.put("accountId", account.getId());

            // -----
            json.put("contactUri", prm.getContactUri());
            json.put("fromUri", prm.getFromUri());
            json.put("toUri", prm.getToUri());
            json.put("body", prm.getMsgBody());
            json.put("contentType", prm.getContentType());

            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toJsonString() {
        return toJson().toString();
    }

}
