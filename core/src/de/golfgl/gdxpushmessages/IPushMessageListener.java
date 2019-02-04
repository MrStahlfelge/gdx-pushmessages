package de.golfgl.gdxpushmessages;

public interface IPushMessageListener {
	/**
	 * called if a registration token was retrieved after initializing the service. If you send this token to
	 * your own server, you might want to check if it has changed since last access.
	 */
	void onRegistrationTokenRetrieved(String token);
}
