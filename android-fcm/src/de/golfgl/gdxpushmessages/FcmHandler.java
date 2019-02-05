package de.golfgl.gdxpushmessages;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This is the Service called by firebase when new messages arrive. Note that this service is not running within libGDX
 * context, your app might not be running at all when this gets called. Also note that if you use Firebase notification,
 * they are not displayed when your game is running but also only sended to your game if you specify data.
 */
public class FcmHandler extends FirebaseMessagingService {
	public static final String KEY_MESSAGE_INTENT = "FCM_BROADCAST";
	public static final String KEY_MESSAGE_INTENT_PAYLOAD = "FCM_DATA";
	protected static final String KEY_RM_PAYLOAD = "payload";

	private LocalBroadcastManager broadcaster;

	@Override
	public void onCreate() {
		super.onCreate();
		broadcaster = LocalBroadcastManager.getInstance(this);
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		String payload = null;
		if (remoteMessage.getData().containsKey(KEY_RM_PAYLOAD))
			payload = remoteMessage.getData().get(KEY_RM_PAYLOAD);

		Intent intent = new Intent(KEY_MESSAGE_INTENT);
		intent.putExtra(KEY_MESSAGE_INTENT_PAYLOAD, payload);
		broadcaster.sendBroadcast(intent);
	}
}
