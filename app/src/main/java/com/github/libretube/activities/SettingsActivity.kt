package com.github.libretube.activities

import android.os.Bundle
import com.github.libretube.R
import com.github.libretube.databinding.ActivitySettingsBinding
import com.github.libretube.extensions.BaseActivity
import com.github.libretube.preferences.MainSettings

class SettingsActivity : BaseActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backImageButton.setOnClickListener {
            onBackPressed()
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, MainSettings())
                .commit()
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.settings)) {
            is MainSettings -> {
                super.onBackPressed()
                finishAndRemoveTask()
            }
            else -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, MainSettings())
                    .commit()
                changeTopBarText(getString(R.string.settings))
            }
        }
    }

    fun changeTopBarText(text: String) {
        if (this::binding.isInitialized) binding.topBarTextView.text = text
    }
}
