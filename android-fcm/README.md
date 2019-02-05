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
    
    
### Instantiation

AndroidLauncher:

		FcmMessageProvider pushMessageProvider = new FcmMessageProvider(this);
		game.pushMessageProvider = pushMessageProvider;
		initialize(game, config);
		Gdx.app.addLifecycleListener(pushMessageProvider);

### Good to know

* You can change the icon, text color and Android O channel for notifications by 
[changing your AndroidManifest.xml](https://firebase.google.com/docs/cloud-messaging/android/client)
* Specifiy the payload for your game in the data key named "payload"
* Note that your listener's `onPushMessageArrived()` method won't get called always if you send a notification message.
See [Firebase doc](https://firebase.google.com/docs/cloud-messaging/android/receive)