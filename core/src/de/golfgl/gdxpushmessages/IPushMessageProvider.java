package de.golfgl.gdxpushmessages;

/**
 * An `IPushMessageProvider` gives you access to the backend and service dependant implementations for a push
 * notificiation service.
 */
public interface IPushMessageProvider {

	/**
	 * Inits the service, if not already done. If successful, a registration token is returned to the listener.
	 * Call this in your game's create() method, or later if you don't want to init the push message provider for
	 * some cases.
	 *
	 * @return true if an initialization attempt was made, false if already initialized
	 */
	boolean initService(IPushMessageListener listener);

	/**
	 * @return registration token for push messages. Note that it might take some time for a token to be retrieved
	 * after {@link #initService(IPushMessageListener)} was called.
	 */
	String getRegistrationToken();

	/**
	 * @return Provider dependant id
	 */
	String getProviderId();

	boolean isInitialized();
}
