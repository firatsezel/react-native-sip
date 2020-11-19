package com.carusto.ReactNativePjSip;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;
import org.pjsip.pjsua2.pjsip_evsub_state;
import org.pjsip.pjsua2.pjsua_buddy_status;

public class PjSipMyBuddy extends Buddy {
	
	public BuddyConfig buddyConfig;

	public PjSipMyBuddy(BuddyConfig config) {
		super();
		this.buddyConfig = config;
	}

	String getStatusText() {
		BuddyInfo bi;

		try {
			bi = getInfo();
		} catch (Exception e) {
			return "?";
		}

		String status = "";
		if (bi.getSubState() == pjsip_evsub_state.PJSIP_EVSUB_STATE_ACTIVE) {
			if (bi.getPresStatus().getStatus() == pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE) {
				status = bi.getPresStatus().getStatusText();
				if (status == null || status.length() == 0) {
					status = "Online";
				}
			} else if (bi.getPresStatus().getStatus() == pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE) {
				status = "Offline";
			} else {
				status = "Unknown";
			}
		}
		return status;
	}

}