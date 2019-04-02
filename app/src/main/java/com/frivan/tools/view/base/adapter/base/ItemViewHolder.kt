package com.frivan.tools.view.base.adapter.base

interface ItemViewHolder<T : ItemData> {

    fun onBind(itemData: T?)

    fun unbind()

}
