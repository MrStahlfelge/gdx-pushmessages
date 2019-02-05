package de.golfgl.gdxpushmessages;

public interface IPushMessageListener {
	/**
	 * called if a registration token was retrieved after initializing the service. If you send this token to
	 * your own server, you might want to check if it has changed since last access.
	 * <p>
	 * Note this might not get called not on the main thread, so make sure to use {@code Gdx.app.postRunnable()} if
	 * needed.
	 */
	void onRegistrationTokenRetrieved(String token);

	/**
	 * Called if a push message with a payload arrived. Note that this might be called if your game is in background
	 * and not on the main thread, so make sure to use {@code Gdx.app.postRunnable()} if needed.
	 *
	 * @param payload
	 */
	void onPushMessageArrived(String payload);
}
