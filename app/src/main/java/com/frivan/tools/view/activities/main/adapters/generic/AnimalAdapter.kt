package com.frivan.tools.view.activities.main.adapters.generic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.frivan.tools.R
import com.frivan.tools.view.base.adapter.base.BaseItemAdapter
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder

class AnimalAdapter() : BaseItemAdapter<AnimalData>(AnimalDiffCalback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<AnimalData> {
        return AnimalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false))
    }

    private class AnimalDiffCalback : DiffUtil.ItemCallback<AnimalData>() {

        override fun areItemsTheSame(oldItem: AnimalData, newItem: AnimalData): Boolean {
            return oldItem.kind == newItem.kind
        }

        override fun areContentsTheSame(oldItem: AnimalData, newItem: AnimalData): Boolean {
            return oldItem.name == newItem.name
        }
    }

}
