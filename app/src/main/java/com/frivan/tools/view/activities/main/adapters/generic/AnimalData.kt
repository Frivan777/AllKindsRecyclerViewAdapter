package com.frivan.tools.view.activities.main.adapters.generic

import com.frivan.tools.R
import com.frivan.tools.view.base.adapter.base.ItemData

data class AnimalData(val kind: Int,
                      val name: String,
                      override val type: Int = R.layout.item_content) : ItemData
