package com.jarvis.assistant.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.data.storage.PreferencesManager
import com.jarvis.assistant.ui.home.HomeActivity
import com.jarvis.assistant.ui.onboarding.OnboardingActivity
import com.jarvis.assistant.ui.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var preferencesManager: PreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ThemeManager.applySplashTheme(this)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, if (preferencesManager.onboardingDone) HomeActivity::class.java else OnboardingActivity::class.java))
            finish()
        }, 2000)
    }
}
