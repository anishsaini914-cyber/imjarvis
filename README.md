# Jarvis AI Assistant

██████╗  █████╗ ██████╗ ██╗   ██╗██╗███████╗
██╔══██╗██╔══██╗██╔══██╗██║   ██║██║██╔════╝
██████╔╝███████║██████╔╝██║   ██║██║███████╗
██╔══██╗██╔══██║██╔══██╗╚██╗ ██╔╝██║╚════██║
██║  ██║██║  ██║██║  ██║ ╚████╔╝ ██║███████║
╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝  ╚═══╝  ╚═╝╚══════╝

A futuristic AI assistant for Android with multi-provider AI support, voice control, and device automation.

## Features

- **Multi-Provider AI**: OpenAI, Google Gemini, and AgentRouter support
- **Voice Control**: Speech-to-text, text-to-speech, wake word detection
- **Global Assistant Mode**: Floating overlay, background listening, call handling
- **In-App Chat Mode**: Modern chat UI with markdown rendering and message history
- **Device Control**: Open apps, flashlight, alarms, browser search, music control
- **Call Handling**: Caller announcement, accept/reject, speaker mode
- **Weather System**: Real-time weather with Open-Meteo API
- **Local Model Support**: GGUF/GGML model import architecture

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM + Repository Pattern
- **DI**: Hilt (Dagger)
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **UI**: Material 3, ViewBinding, RecyclerView

## Build Requirements

- Java 17
- Gradle 8.7
- Android Gradle Plugin 8.5.0
- Kotlin 1.9.24
- compileSdk: 34
- targetSdk: 34
- minSdk: 26

## Build Instructions

### Local Build
```bash
./gradlew assembleDebug
```

### GitHub Codespaces
The project includes `.devcontainer/` configuration for automatic setup. Just open in Codespaces and run:
```bash
./gradlew assembleDebug
```

### GitHub Actions
Build automatically via `.github/workflows/android-build.yml`. APK is uploaded as artifact.

## Configuration

### API Keys (Required)
Set your API keys in the app's AI Provider Settings screen:
- OpenAI API Key
- Google Gemini API Key
- AgentRouter API Key

### Permissions
Grant the following permissions for full functionality:
- Microphone
- Overlay (System Alert Window)
- Notifications
- Phone State
- Notification Listener Access
- Disable Battery Optimization

## Developer

**ANISH SAINI**
Email: anishsaini939@gmail.com

## License

This project is for educational purposes.
