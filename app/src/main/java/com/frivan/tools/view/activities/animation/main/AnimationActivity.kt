package com.frivan.tools.view.activities.animation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.frivan.tools.R
import com.frivan.tools.view.activities.animation.share.image.ShareImageActivity
import com.frivan.tools.view.activities.animation.share.list.ContactListActivity
import com.frivan.tools.view.fragments.animation.dynamic.DynamicFragment
import com.frivan.tools.view.fragments.animation.explode.ExplodeFragment
import com.frivan.tools.view.fragments.animation.image.ImageFragment
import com.frivan.tools.view.fragments.animation.simple.SimpleFragment
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        setSupportActionBar(this.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_animation, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_simple -> {
                this.showFragment(SimpleFragment.newInstance())

                true
            }

            R.id.action_explode -> {
                this.showFragment(ExplodeFragment.newInstance())

                true
            }
            R.id.action_image -> {
                this.showFragment(ImageFragment.newInstance())

                true
            }
            R.id.action_activity_share -> {
                this.startActivity(ContactListActivity.newIntent(this))

                true
            }
            R.id.action_fragment_share -> {
                this.startActivity(ShareImageActivity.newIntent(this))

                true
            }
            R.id.action_dynamic -> {
                this.showFragment(DynamicFragment.newInstance())

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFragment(fragment: Fragment) {
        this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
    }
}
