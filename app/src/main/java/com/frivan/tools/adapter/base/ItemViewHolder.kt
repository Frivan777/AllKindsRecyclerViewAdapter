package com.frivan.tools.adapter.base

interface ItemViewHolder<T : ItemData> {

    fun onBind(itemData: T?)

    fun unbind()

}
