# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Retrofit
-keepattributes Signature
-keepattributes Exceptions
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep Gson
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep Model classes
-keep class com.jarvis.assistant.domain.model.** { *; }
-keep class com.jarvis.assistant.data.database.** { *; }
-keep class com.jarvis.assistant.data.local.entity.** { *; }
-keep class com.jarvis.assistant.data.remote.dto.** { *; }

# Keep Markwon
-dontwarn io.noties.markwon.**

# Keep Lottie
-dontwarn com.airbnb.lottie.**

# Keep Glide
-keep class com.bumptech.glide.** { *; }

# Keep Hilt generated
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }

# Keep XML serialization
-keepclassmembers class * {
    @androidx.room.Entity <fields>;
}

# Security
-dontwarn org.bouncycastle.**

# Enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
