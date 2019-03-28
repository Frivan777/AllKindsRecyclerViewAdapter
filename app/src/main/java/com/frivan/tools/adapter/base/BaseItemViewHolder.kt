package com.frivan.tools.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseItemViewHolder<T : ItemData>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    ItemViewHolder<T>,
    LayoutContainer {

    // region ItemViewHolder

    override fun unbind() {

    }

    //endregion ItemViewHolder

}
