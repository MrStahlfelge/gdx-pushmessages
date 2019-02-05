package de.golfgl.gdxpushmessages;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 * IPushMessageProvider for Firebase on Android. Don't forget to drop jour google-services.json and use the
 * Google Services plugin in your Android project. Also do not forget to add this as your game's lifecyclelistener
 * with {@code Gdx.app.addLifecycleListener()}.
 */
public class FcmMessageProvider implements IPushMessageProvider, LifecycleListener {
	protected static final String PROVIDER_ID = "FCM";
	private final Activity activity;
	private final BroadcastReceiver mMessageReceiver;
	protected String registrationToken;
	private IPushMessageListener listener;

	public FcmMessageProvider(Activity activity) {
		this.activity = activity;

		mMessageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (listener != null) {
					String payload = intent.getExtras().getString(FcmHandler.KEY_MESSAGE_INTENT_PAYLOAD);
					listener.onPushMessageArrived(payload);
				}
			}
		};
	}

	@Override
	public boolean initService(final IPushMessageListener listener) {
		if (this.listener == null && registrationToken == null) {
			this.listener = listener;
			LocalBroadcastManager.getInstance(activity).registerReceiver((mMessageReceiver),
					new IntentFilter(FcmHandler.KEY_MESSAGE_INTENT));

			// TODO Intent aus Notification auslesen

			Gdx.app.log(PROVIDER_ID, "Retrieving FCM token...");
			FirebaseInstanceId.getInstance().getInstanceId()
					.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
						@Override
						public void onComplete(@NonNull Task<InstanceIdResult> task) {
							if (!task.isSuccessful()) {
								// this happens when run on a device without Play Services
								Gdx.app.error(PROVIDER_ID, "getInstanceId failed", task.getException());
								return;
							}
							// Get new Instance ID token
							registrationToken = task.getResult().getToken();
							Gdx.app.log(PROVIDER_ID, "Retrieved FCM token: " + registrationToken);

							if (FcmMessageProvider.this.listener != null) {
								FcmMessageProvider.this.listener.onRegistrationTokenRetrieved(registrationToken);
							}
						}
					});

			return true;
		}
		return false;
	}

	@Override
	public String getRegistrationToken() {
		return registrationToken;
	}

	@Override
	public String getProviderId() {
		return PROVIDER_ID;
	}

	@Override
	public boolean isInitialized() {
		return registrationToken != null;
	}

	@Override
	public void pause() {
		// when the game is paused, there is nothing to do
	}

	@Override
	public void resume() {
		// when the game is resumed, there is nothing to do
	}

	@Override
	public void dispose() {
		// unregister broadcastreceiver
		LocalBroadcastManager.getInstance(activity).unregisterReceiver(mMessageReceiver);
	}
}
