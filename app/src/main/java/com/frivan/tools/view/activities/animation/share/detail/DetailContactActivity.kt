package com.frivan.tools.view.activities.animation.share.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.frivan.tools.R
import kotlinx.android.synthetic.main.activity_detail_contact.*

class DetailContactActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_NAME = "name"

        private const val EXTRA_IMAGE = "image"

        fun newIntent(context: Context, name: String, image: Drawable): Intent {
            return Intent(context, DetailContactActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_IMAGE, image.toBitmap())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_contact)

        this.initView()
    }

    private fun initView() {
        this.name.text = this.intent.getStringExtra(EXTRA_NAME)
        this.icon.setImageBitmap(this.intent.getParcelableExtra(EXTRA_IMAGE))
    }
}
