## Apple Push Notification Service

### Setup

Add the dependency to the iOS RoboVM project:

     compile "de.golfgl.gdxpushmessages:gdx-pushmessages-ios-apn:$pushMsgsVersion"
    
    
### Instantiation

In your IOSLauncher:

		game.pushMessageProvider = new ApnsMessageProvider(requestPermissions);
        return new IOSApplication(game, config);

Set `requestPermissions`-Flag to `true` if you want the message provider to request notification permissions before
requesting a pushtoken. This is useful if you want to present alert messages to your users. Set it to `false` if you only
want to use silent push notifications.

It is also important that you change your IOSLauncher in a way that it extends `ApnsAppDelegate`:

    public class IOSLauncher extends ApnsAppDelegate {
        ...
    }

iOS calls your app delegate for received tokens and notifications, and the provided superclass will forward this to the
message provider which needs this information.

### Usage

As always, a call to `ApnsMessageProvider.initService()` is needed to setup your game for listening to push messages.
The first time you call the method on a user's device, a push token and based on your settings notification permission is requested.

APNs supports alert and data messages and messages consisting of both. You can send alerts and do not need to implement code in your game. They
will be displayed when your game is not running in foreground.

If it is necessary for your game to know that a notification has arrived, you need to add a "payload" data to the message. Such messages will
make your listener's `onPushMessageArrived` method get called.
