package com.carusto.ReactNativePjSip;

import com.carusto.ReactNativePjSip.dto.AccountConfigurationDTO;
import java.util.ArrayList;
import org.json.JSONObject;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;

public class PjSipAccount extends Account {

    public ArrayList<PjSipMyBuddy> buddyList = new ArrayList<PjSipMyBuddy>();

    private static String TAG = "PjSipAccount";

    /**
     * Last registration reason.
     */
    private String reason;

    private PjSipService service;

    private AccountConfigurationDTO configuration;

    private Integer transportId;

    public PjSipAccount(PjSipService service, int transportId, AccountConfigurationDTO configuration) {
        this.service = service;
        this.transportId = transportId;
        this.configuration = configuration;
    }

    public void register(boolean renew) throws Exception {
        setRegistration(renew);
    }

    public PjSipMyBuddy addBuddy(BuddyConfig bud_cfg) {
		PjSipMyBuddy bud = new PjSipMyBuddy(bud_cfg);
		try {
			bud.create(this, bud_cfg);
		} catch (Exception e) {
			bud.delete();
			bud = null;
		}

		if (bud != null) {
			buddyList.add(bud);
			if (bud_cfg.getSubscribe()) {
				try {
					bud.subscribePresence(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
		}
		return bud;
	}

	public void delBuddy(PjSipMyBuddy buddy) {
		buddyList.remove(buddy);
		buddy.delete();
	}

	public void delBuddy(int index) {
		PjSipMyBuddy bud = buddyList.get(index);
		buddyList.remove(index);
		bud.delete();
	}

    public PjSipService getService() {
        return service;
    }

    public int getTransportId() {
        return transportId;
    }

    public AccountConfigurationDTO getConfiguration() {
        return configuration;
    }

    public String getRegistrationStatusText() {
        try {
            return getInfo().getRegStatusText();
        } catch (Exception e) {
            return "Connecting...";
        }
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        reason = prm.getReason();
        service.emmitRegistrationChanged(this, prm);
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        PjSipCall call = new PjSipCall(this, prm.getCallId());
        service.emmitCallReceived(this, call);
    }

    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        PjSipMessage message = new PjSipMessage(this, prm);
        service.emmitMessageReceived(this, message);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        try {
            JSONObject registration = new JSONObject();
            registration.put("status", getInfo().getRegStatus());
            registration.put("statusText", getInfo().getRegStatusText());
            registration.put("active", getInfo().getRegIsActive());
            registration.put("reason", reason);

            json.put("id", getId());
            json.put("uri", getInfo().getUri());
            json.put("name", configuration.getName());
            json.put("username", configuration.getUsername());
            json.put("domain", configuration.getDomain());
            json.put("password", configuration.getPassword());
            json.put("proxy", configuration.getProxy());
            json.put("transport", configuration.getTransport());

            json.put("contactParams", configuration.getContactParams());
            json.put("contactUriParams", configuration.getContactUriParams());

            json.put("regServer", configuration.getRegServer());
            json.put("regTimeout", configuration.isRegTimeoutNotEmpty() ? String.valueOf(configuration.getRegTimeout()) : "");
            json.put("regContactParams", configuration.getRegContactParams());
            json.put("regHeaders", configuration.getRegHeaders());
            json.put("regOnAdd", configuration.isRegOnAdd());

            json.put("registration", registration);

            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toJsonString() {
        return toJson().toString();
    }
}
