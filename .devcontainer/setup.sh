#!/bin/bash

echo "=================================="
echo "  JARVIS Android Build Setup"
echo "=================================="

set -e

export ANDROID_HOME=/opt/android-sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/34.0.0

echo "ANDROID_HOME=$ANDROID_HOME"

# Add to bashrc for future sessions
echo "export ANDROID_HOME=$ANDROID_HOME" >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/34.0.0' >> ~/.bashrc

echo ""
echo "Java version:"
java -version 2>&1 || echo "Java not found!"

echo ""
echo "Android SDK:"
ls -la $ANDROID_HOME/platforms/ 2>/dev/null || echo "No platforms found"

echo ""
echo "Making gradlew executable..."
chmod +x /workspaces/jarvis/gradlew

# Download Gradle wrapper jar if missing
WRAPPER_JAR="/workspaces/jarvis/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
    echo ""
    echo "Downloading gradle-wrapper.jar..."
    curl -sL "https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar" -o "$WRAPPER_JAR" 2>/dev/null || \
    curl -sL "https://services.gradle.org/distributions/gradle-8.7-wrapper.jar" -o "$WRAPPER_JAR" 2>/dev/null || \
    wget -q "https://services.gradle.org/distributions/gradle-8.7-wrapper.jar" -O "$WRAPPER_JAR" 2>/dev/null || \
    echo "NOTE: gradle-wrapper.jar not found. Run 'gradle wrapper --gradle-version 8.7' manually if needed."
fi

echo ""
echo "Building project..."
cd /workspaces/jarvis && ./gradlew assembleDebug --stacktrace --no-daemon

if [ $? -eq 0 ]; then
    echo ""
    echo "=================================="
    echo "  BUILD SUCCESSFUL!"
    echo "  APK at: app/build/outputs/apk/debug/app-debug.apk"
    echo "=================================="
else
    echo ""
    echo "=================================="
    echo "  BUILD FAILED - Check logs above"
    echo "=================================="
    exit 1
fi
