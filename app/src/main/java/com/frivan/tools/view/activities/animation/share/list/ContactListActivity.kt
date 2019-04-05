package com.frivan.tools.view.activities.animation.share.list

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.toAndroidPair
import com.frivan.tools.R
import com.frivan.tools.view.activities.animation.share.detail.DetailContactActivity
import com.frivan.tools.view.activities.animation.share.list.adapter.ContactAdapter
import com.frivan.tools.view.activities.animation.share.list.adapter.data.ContactData
import com.frivan.tools.view.activities.animation.share.list.adapter.data.HeaderData
import com.frivan.tools.view.base.adapter.base.ItemData
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlin.random.Random


private const val CONTACT_COUNT = 50

private val ICONS = arrayOf(
        R.drawable.contact1, R.drawable.contact2, R.drawable.contact3, R.drawable.contact4,
        R.drawable.contact5, R.drawable.contact6, R.drawable.contact7, R.drawable.contact8,
        R.drawable.contact9, R.drawable.contact10, R.drawable.contact11, R.drawable.contact12,
        R.drawable.contact13, R.drawable.contact14, R.drawable.contact15, R.drawable.contact16,
        R.drawable.contact17, R.drawable.contact18
)

private val NAMES = arrayOf(
        "Agatha MacDonald", "Albert O’Connor", "Adam Gordon", "Amanda Brian", "Alexander Chester",
        "Andrew Grant", "Anna Ross", "Augusta Walter", "Audrey Smith", "Allan Butler",
        "Alison Black", "Barrett Nash-Williams", "Bruce Robertson", "Boris Jones", "Carl Murphy",
        "Carla Williams", "Catherine Edwards", "Cliff Birds"
)

class ContactListActivity : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, ContactListActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_contact_list)

        this.initView()
    }


    private fun initView() {
        val items = mutableListOf<ItemData>()

        (0..CONTACT_COUNT).map {
            val index = Random.nextInt(0, ICONS.size)

            ContactData(NAMES[index], ICONS[index])
        }.sortedBy {
            it.name
        }.forEachIndexed { index, contactData ->
            when {
                index == 0 -> items.add(HeaderData(contactData.name[0].toUpperCase().toString()))
                (items.last() as? ContactData)?.name?.get(0)?.equals(contactData.name[0], true) == false -> {
                    items.add(HeaderData(contactData.name[0].toUpperCase().toString()))
                }
                else -> items.add(contactData)
            }
        }

        val adapter = ContactAdapter {
            this.showDetailScreen(it.toAndroidPair())
        }.apply {
            submitList(items)
        }

        this.recyclerView.adapter = adapter
    }

    private fun showDetailScreen(pair: Pair<Int, View>) {
        val adapter = recyclerView.adapter as? ContactAdapter ?: return

        val data = adapter.currentList[pair.first] as? ContactData ?: return
        val drawable = this.getDrawable(data.icon) ?: return

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val view = pair.second
            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair<View, String>(view.icon,
                            this.getString(R.string.detail_transition_name_icon)),
                    Pair<View, String>(view.name,
                            this.getString(R.string.detail_transition_name_title)))

            this.startActivity(DetailContactActivity.newIntent(this,
                    data.name,
                    drawable), options.toBundle())
        } else {
            this.startActivity(DetailContactActivity.newIntent(this,
                    data.name,
                    drawable))
        }
    }
}
