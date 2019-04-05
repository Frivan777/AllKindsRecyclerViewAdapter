package com.frivan.tools.view.activities.animation.share.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.frivan.tools.R
import com.frivan.tools.view.activities.animation.share.list.adapter.holder.ContactViewHolder
import com.frivan.tools.view.activities.animation.share.list.adapter.holder.HeaderViewHolder
import com.frivan.tools.view.base.adapter.base.BaseItemAdapter
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder
import com.frivan.tools.view.base.adapter.base.ItemData

class ContactAdapter(private val itemClick: ((Pair<Int, View>) -> Unit)? = null) : BaseItemAdapter<ItemData>(AllKindsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<ItemData> {
        return when (viewType) {
            R.layout.item_contact -> ContactViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_contact,
                            parent,
                            false
                    )
            ) {
                this.itemClick?.invoke(it)
            }
            else ->
                HeaderViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_contact_header,
                                parent,
                                false
                        )
                )
        }
    }

    private class AllKindsDiffCallback : DiffUtil.ItemCallback<ItemData>() {

        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem == newItem
        }

    }
}
