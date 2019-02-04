package de.golfgl.gdxpushmessages;

import android.content.Context;
import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class FcmMessageProvider implements IPushMessageProvider {
	protected static final String PROVIDER_ID = "FCM";
	protected String registrationToken;
	private IPushMessageListener listener;

	@Override
	public boolean initService(final IPushMessageListener listener) {
		//TODO check what happens without Play Services

		if (this.listener == null && registrationToken == null) {
			this.listener = listener;

			Gdx.app.log(PROVIDER_ID, "Retrieving FCM token...");
			FirebaseInstanceId.getInstance().getInstanceId()
					.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
						@Override
						public void onComplete(@NonNull Task<InstanceIdResult> task) {
							if (!task.isSuccessful()) {
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
}
