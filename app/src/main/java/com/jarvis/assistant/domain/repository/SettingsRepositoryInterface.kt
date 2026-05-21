package com.jarvis.assistant.domain.repository

interface SettingsRepositoryInterface {
    var selectedProvider: String; var openAiModel: String; var geminiModel: String; var agentRouterModel: String
    var wakeWord: String; var wakeWordEnabled: Boolean; var overlayEnabled: Boolean; var ttsEnabled: Boolean
    var callAnnouncement: Boolean; var weatherUnit: String; var latitude: Float; var longitude: Float; var onboardingDone: Boolean
}
