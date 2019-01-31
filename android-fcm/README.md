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

