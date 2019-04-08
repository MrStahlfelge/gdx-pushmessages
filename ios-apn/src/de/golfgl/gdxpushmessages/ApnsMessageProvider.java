package de.golfgl.gdxpushmessages;

import com.badlogic.gdx.Gdx;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIRemoteNotification;
import org.robovm.apple.usernotifications.UNAuthorizationOptions;
import org.robovm.apple.usernotifications.UNAuthorizationStatus;
import org.robovm.apple.usernotifications.UNNotificationSettings;
import org.robovm.apple.usernotifications.UNUserNotificationCenter;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.objc.block.VoidBlock2;

/**
 * IPushMessageProvider for APNS on iOS (RoboVM). Don't forget to use {@link ApnsAppDelegate} as your
 * IOSLauncher's superclass
 * */
public class ApnsMessageProvider implements IPushMessageProvider {
    protected static final String PROVIDER_ID = "APNS";
    protected static final String KEY_RM_PAYLOAD = "payload";
    private static String pushToken;
    private static boolean isRequestingToken;
    private static IPushMessageListener listener;
    private final boolean requestNotificationPermission;

	/**
	 * @param requestNotificationPermission if set to true, user permission for showing notifications is requested
	 *                                         before retrieving a push token. Use this if you want to show
	 *                                         notification messages to the user
	 */
	public ApnsMessageProvider(boolean requestNotificationPermission) {
		this.requestNotificationPermission = requestNotificationPermission;
	}

	@Override
    public boolean initService(IPushMessageListener listener) {
        if (pushToken != null || isRequestingToken || this.listener != null)
            return false;

        this.listener = listener;

        isRequestingToken = true;
        if (requestNotificationPermission)
        	requestNotificationPermission();
        else
			requestPushtoken();

		return true;
    }

	/**
	 * requests a push token. Callback from iOS comes to AppDelegate methods, which need to forward back to this class.
	 */
	protected void requestPushtoken() {
		Gdx.app.debug(PROVIDER_ID, "Requesting push token...");
		UIApplication.getSharedApplication().registerForRemoteNotifications();
	}

	/**
     * Requests permission for showing notifications with badges, sound and alert.
     */
    protected void requestNotificationPermission() {
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorization(UNAuthorizationOptions.with(UNAuthorizationOptions.Badge,
                UNAuthorizationOptions.Alert, UNAuthorizationOptions.Sound), new VoidBlock2<Boolean, NSError>() {
            @Override
            public void invoke(Boolean granted, NSError nsError) {
                if (granted)
					getNotificationSettingsAndRequestPushtoken();
                else {
					isRequestingToken = false;
                    Gdx.app.log(PROVIDER_ID, "User declined authorization for notifications.");
                }
            }
        });
    }

	protected void getNotificationSettingsAndRequestPushtoken() {
    	if (Foundation.getMajorSystemVersion() >= 10)
			UNUserNotificationCenter.currentNotificationCenter().getNotificationSettings(new VoidBlock1<UNNotificationSettings>() {
				@Override
				public void invoke(UNNotificationSettings unNotificationSettings) {
					if (unNotificationSettings.getAuthorizationStatus().equals(UNAuthorizationStatus.Authorized)) {
						requestPushtokenOnMainThread();
					} else {
						Gdx.app.log(PROVIDER_ID, "User declined authorization");
						isRequestingToken = false;
					}
				}
			});
    	else {
    		// TODO check for notification authorization on iOS <= 9
    		requestPushtokenOnMainThread();
		}
	}

	protected void requestPushtokenOnMainThread() {
		DispatchQueue.getMainQueue().async(new Runnable() {
			@Override
			public void run() {
				requestPushtoken();
			}
		});
	}

	@Override
    public String getRegistrationToken() {
        return pushToken;
    }

    @Override
    public String getProviderId() {
        return PROVIDER_ID;
    }

    @Override
    public boolean isInitialized() {
        return pushToken != null;
    }

    static void didRegisterForRemoteNotifications(boolean success, byte[] token) {
        isRequestingToken = false;
        pushToken = byteToHex(token);
        if (success) {
            Gdx.app.log(PROVIDER_ID, "Retrieved token: " + pushToken);
            if (listener != null)
                listener.onRegistrationTokenRetrieved(pushToken);
        } else
            Gdx.app.error(PROVIDER_ID, "Failure retrieving push token");
    }

    static void pushMessageArrived(UIRemoteNotification remoteNotification) {
        if (listener == null) {
            Gdx.app.log(PROVIDER_ID, "Message arrived, no listener.");
            return;
        }

        listener.onPushMessageArrived(remoteNotification.getString(KEY_RM_PAYLOAD));
    }

    public static String byteToHex(byte[] num) {
        char[] hexDigits = new char[2 * num.length];

        for (int i = 0; i < num.length; i++) {
            hexDigits[i * 2] = Character.forDigit((num[i] >> 4) & 0xF, 16);
            hexDigits[i * 2 + 1] = Character.forDigit((num[i] & 0xF), 16);
        }
        return new String(hexDigits);
    }
}
