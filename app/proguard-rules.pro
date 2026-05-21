-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

-keep class com.jarvis.assistant.data.remote.** { *; }

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

-keepclassmembers class * {
    @com.google.dagger.hilt.android.EarlyEntryPoint <methods>;
}

-keep class * extends android.app.Service

-keep class * extends android.content.BroadcastReceiver

-keep class * extends android.app.Activity {
    public void *(android.view.View);
}
