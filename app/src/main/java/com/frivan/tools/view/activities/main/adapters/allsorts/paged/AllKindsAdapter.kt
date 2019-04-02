package com.frivan.tools.view.activities.main.adapters.allsorts.paged

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.frivan.tools.R
import com.frivan.tools.view.activities.main.adapters.allsorts.ContentViewHolder
import com.frivan.tools.view.activities.main.adapters.allsorts.DelimiterViewHolder
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder
import com.frivan.tools.view.base.adapter.base.BasePagedListItemAdapter
import com.frivan.tools.view.base.adapter.base.ItemData


class AllKindsAdapter : BasePagedListItemAdapter<ItemData>(AllKindsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<ItemData> {
        return when (viewType) {
            R.layout.item_delimiter -> DelimiterViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_delimiter,
                            parent,
                            false))
            R.layout.item_content -> ContentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_content,
                            parent,
                            false))
            else -> ContentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_placeholder,
                            parent,
                            false))
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
