package com.frivan.tools.view.base.adapter.base

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseItemAdapter<T : ItemData> : ListAdapter<T, BaseItemViewHolder<T>> {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)

    constructor(config: AsyncDifferConfig<T>) : super(config)

    //region RecyclerView.Adapter

    override fun onBindViewHolder(holder: BaseItemViewHolder<T>, position: Int) {
        holder.onBind(this.getItem(position))
    }

    override fun onViewRecycled(holder: BaseItemViewHolder<T>) {
        holder.unbind()

        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return this.getItem(position).type
    }

    //endregion RecyclerView.Adapter

}
