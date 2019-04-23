# gdx-pushmessages

Use Push Message services in your libGDX game.

## Supported game services

* [Firebase Cloud Messaging](https://github.com/MrStahlfelge/gdx-pushmessages/tree/master/android-fcm) (Android)
* [Apple Push Notification Service](https://github.com/MrStahlfelge/gdx-pushmessages/tree/master/ios-apn) (iOS)

## Basic concept

The library provides an interface `IPushMessageProvider` that you reference in your core code. Your platform-dependant 
launchers instantiate an actual implementation of the interface.

Every implemented game service client has its own project you can decide to include or not.

See the corresponding [example project](https://github.com/MrStahlfelge/gdx-pushmessages-app) and the subprojects 
readme files for further documentation.

## Installation

This project is published to the Sonatype Maven repository. You can integrate the lib into your project by just adding the dependencies to your `build.gradle` file.

Define the version of this API right after the gdxVersion:

    gdxVersion = '1.9.8' //or a later gdx version you use
    pushMsgsVersion = '1.0.0'

Then add the needed dependencies to each subproject:

Core:

    compile "de.golfgl.gdxpushmessages:gdx-pushmessages-core:$pushMsgsVersion"

HTML:

    compile "de.golfgl.gdxpushmessages:gdx-pushmessages-core:$pushMsgsVersion:sources"


Add the following line to your HTML GdxDefinition.gwt.xml:

    <inherits name="de.golfgl.gdxpushmessages.gdx_pushmsgs_gwt" />

### Building from source
To build from source, clone or download this repository, then open it in Android Studio. Perform the following command to compile and upload the library in your local repository:

    gradlew clean uploadArchives -PLOCAL=true

See `build.gradle` file for current version to use in your dependencies.


## Usage

Initialize the push message client when you need to have it active (at game start or after your user consented to 
receive messages):

     pushProvider.initService(listener)

The listener is a class implementing `IPushMessageListener` provided by you if you need it. It will receive push 
messages while your game is running. Handling messages when your game is not running depends of the used platform and
 service, see the subprojects for documentation. 

### News & Community

You can get help on the [libgdx discord](https://discord.gg/6pgDK9F).


# License

The project is licensed under the Apache 2 License, meaning you can use it free of charge, without strings attached in commercial and non-commercial projects. We love to get (non-mandatory) credit in case you release a game or app using this project!
