package com.frivan.tools.view.activities.animation.share.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.frivan.tools.R

class ImageActivity : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, ImageActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setupTransition()

        setContentView(R.layout.activity_image)
    }

    private fun setupTransition() {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            enterTransition = Explode().apply {
                addTarget(R.id.textView)
            }
        }
    }
}
