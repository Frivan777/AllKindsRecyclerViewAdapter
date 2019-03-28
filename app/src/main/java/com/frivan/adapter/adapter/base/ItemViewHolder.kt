package com.frivan.adapter.adapter.base

interface ItemViewHolder<T : ItemData> {

    fun onBind(itemData: T?)

    fun unbind()

}
