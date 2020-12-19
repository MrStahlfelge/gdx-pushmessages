## Firebase Cloud Messaging

### Setup

Add google services plugin to your main build.gradle:

    buildscript {
        // ...
        dependencies {
            // ...
            classpath 'com.google.gms:google-services:4.2.0' // google-services plugin
        }
    }

If the plugin can't be found, add the Google Maven repository to your buildscript repositories:

    maven { url 'https://maven.google.com' }
    
or, if you use Gradle 4 or later

    google()
    
Drop the `google-services.json` file that you obtained from Firebase into your `android` subfolder and add the 
following line at the bottom of the `android/build.gradle` file:

    apply plugin: 'com.google.gms.google-services'


Add the dependency to the Android project:

    compile "de.golfgl.gdxpushmessages:gdx-pushmessages-android-fcm:$pushMsgsVersion"
    


| :information_source: Note | Firebase Analytics is not included and Firebase Cloud Messanging's auto-init feature is disabled to respect the privacy of your users. |
| --- | --- |


    
### Instantiation

AndroidLauncher:

		FcmMessageProvider pushMessageProvider = new FcmMessageProvider(this);
		game.pushMessageProvider = pushMessageProvider;
		initialize(game, config);
		Gdx.app.addLifecycleListener(pushMessageProvider);
		
### Usage

As always, a call to `FcmMessageProvider.initService()` is needed to setup your game for listening to push messages.
The first time you call the method on a user's device, Firebase will do the setup for receiving push messages for your 
game. 

Firebase supports notification and data messages and messages consisting of both. It is important to understand the 
difference: [Firebase documentation](https://firebase.google.com/docs/cloud-messaging/android/receive)

You can send notification messages from your Firebase console and do not need to implement code in your game. They 
will be displayed when your game is not running in foreground. You can change the icon, text color and Android O channel 
for notification messages by [changing your AndroidManifest.xml](https://firebase.google
.com/docs/cloud-messaging/android/client).

If it is necessary for your game to know that a notification has arrived, you need to send a data message or data and 
notification message. The incoming message must get processed by a handler service. `android-fcm` comes with `FcmMessageHandler` 
that posts all incoming messages with a "payload" data key to your `IPushMessageListener`'s `onPushMessageArrived()` 
method when your game is running. You can use it by extending your `AndroidManifest` with the following lines inside 
your `application` node:

		<service android:name="de.golfgl.gdxpushmessages.FcmMessageHandler">
			<intent-filter>
				<action android:name="com.google.firebase.MESSAGING_EVENT"/>
			</intent-filter>
		</service>

If you need other behaviour, extend it and specify your own class in the service tag. 
