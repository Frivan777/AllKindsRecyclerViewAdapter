package com.frivan.tools.adapter.base

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil

const val PLACEHOLDER_TYPE = -1

abstract class BasePagedListItemAdapter<T : ItemData> : PagedListAdapter<T, BaseItemViewHolder<T>> {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)

    constructor(config: AsyncDifferConfig<T>) : super(config)

    //region PagedListAdapter

    override fun onBindViewHolder(holder: BaseItemViewHolder<T>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewRecycled(holder: BaseItemViewHolder<T>) {
        holder.unbind()

        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.type ?: PLACEHOLDER_TYPE
    }

    //endregion PagedListAdapter

}
