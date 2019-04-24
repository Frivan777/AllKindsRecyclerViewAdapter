package com.frivan.tools.view.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frivan.tools.R
import com.frivan.tools.view.activities.animation.main.AnimationActivity
import com.frivan.tools.view.activities.main.datasourse.ItemDataSource
import com.frivan.tools.view.activities.main.datasourse.ItemDataSourceFactory
import com.frivan.tools.view.base.adapter.base.ItemData
import com.frivan.tools.view.decoration.LoadingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val PAGE_SIZE = 10

private const val EXTRA_POSITION = "extraPosition"

private const val REFRESH_DELAY = 2000L

private const val UP_SCROLL_DIRECTION = -1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            this.startActivity(Intent(this, AnimationActivity::class.java))
        }

        this.initView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()?.let {
            outState.putInt(EXTRA_POSITION, it)
        }

        super.onSaveInstanceState(outState)
    }

    private fun initView(savedInstanceState: Bundle?) {

        //region Paged AllKindsAdapter

        val adapter = com.frivan.tools.view.activities.main.adapters.allsorts.paged.AllKindsAdapter()

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE * 2) //default = PageSize * 3
                .setPrefetchDistance(PAGE_SIZE) //default = PageSize
                .build()

        val itemDataSourceFactory = ItemDataSourceFactory()

        val pagedListData = LivePagedListBuilder<Int, ItemData>(itemDataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(object : PagedList.BoundaryCallback<ItemData?>() {
                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                    }

                    override fun onItemAtEndLoaded(itemAtEnd: ItemData) {
                        super.onItemAtEndLoaded(itemAtEnd)
                    }

                    override fun onItemAtFrontLoaded(itemAtFront: ItemData) {
                        super.onItemAtFrontLoaded(itemAtFront)
                    }
                })
                .also {
                    savedInstanceState?.getInt(EXTRA_POSITION)?.let { position ->
                        Log.d(this.javaClass.name, "setInitialKey=$position")
                        it.setInitialLoadKey(position)
                    }
                }.build()

        pagedListData.observe(this, Observer { })

        val loadingItemDecoration = LoadingItemDecoration()

        val itemDataSource = ItemDataSource({
            this.recyclerView.addItemDecoration(loadingItemDecoration)
        }, {
            this.recyclerView.removeItemDecoration(loadingItemDecoration)
            this.endUpdate()
        })

        val pagedList = PagedList.Builder(itemDataSource, config)
                .setFetchExecutor(MainThreadExecutor())
                .setNotifyExecutor(MainThreadExecutor())
                .also {
                    savedInstanceState?.getInt(EXTRA_POSITION)?.let { position ->
                        it.setInitialKey(position)
                    }
                }
                .build()

        adapter.submitList(pagedList)

        this.recyclerView.adapter = adapter

        //endregion Paged AllKindsAdapter

        // region AnimalAdapter
//        val adapter = AnimalAdapter()
//
//        this.recyclerView.adapter = adapter
//
//        var list = mutableListOf<AnimalData>()
//
//        list.add(AnimalData(0, "Петух"))
//        list.add(AnimalData(1, "Курица"))
//        list.add(AnimalData(2, "Цыпленок"))
//        list.add(AnimalData(3, "Воробей"))
//        list.add(AnimalData(4, "Ворона"))
//
//        adapter.submitList(list)
//
//        Handler().postDelayed(Runnable {
//            list = mutableListOf()
//
//            list.add(AnimalData(0, "Петух"))
//            list.add(AnimalData(2, "Цыпленок"))
//            list.add(AnimalData(3, "Воробей"))
//            list.add(AnimalData(1, "Курица"))
//            list.add(AnimalData(4, "Ворона"))
//
//            adapter.submitList(list)
//        }, 1000)
        // endregion AnimalAdapter

        //region Simple AllKindsAdapter

//        val adapter = AllKindsAdapter()
//        var list = mutableListOf<ItemData>()
//
//        this.recyclerView.adapter = adapter
//
//        list.add(ContentData(ITEM_CONTENT, "Content 1"))
//        list.add(ContentData(ITEM_CONTENT, "Content 2"))
//        list.add(ContentData(ITEM_CONTENT, "Content 3"))
//        list.add(DelimiterData(ITEM_DELIMITER))
//        list.add(ContentData(ITEM_CONTENT, "Content 4"))
//        list.add(ContentData(ITEM_CONTENT, "Content 5"))
//        list.add(ContentData(ITEM_CONTENT, "Content 6"))
//        list.add(DelimiterData(ITEM_DELIMITER))
//
//        adapter.submitList(list)
//
//        Handler().postDelayed(Runnable {
//            list = mutableListOf<ItemData>()
//
//            list.add(ContentData(ITEM_CONTENT, "Content 1"))
//            list.add(ContentData(ITEM_CONTENT, "Content 2"))
//            list.add(ContentData(ITEM_CONTENT, "Content 3"))
//            list.add(ContentData(ITEM_CONTENT, "Content 4"))
//            list.add(ContentData(ITEM_CONTENT, "Content 5"))
//            list.add(ContentData(ITEM_CONTENT, "Content 6"))
//            list.add(DelimiterData(ITEM_DELIMITER))
//
//            adapter.submitList(list)
//        }, 1000)

        //endregion Simple AllKindsAdapter

        //region View settings

        val typedValue = TypedValue()
        this.theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, typedValue, true)
        val offset = TypedValue.complexToDimensionPixelSize(
                typedValue.data,
                resources.displayMetrics
        )
        this.swipeRefresh.isEnabled = false
        this.swipeRefresh.setProgressViewOffset(false, 0, offset)
        this.swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                this.endUpdate()
            }, REFRESH_DELAY)
        }

        this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(UP_SCROLL_DIRECTION)) {
                    this@MainActivity.swipeRefresh.isEnabled = true
                } else if (swipeRefresh.isEnabled && !swipeRefresh.isRefreshing) {
                    this@MainActivity.swipeRefresh.isEnabled = false
                }
            }

        })

        //endregion View settings
    }

    private fun endUpdate() {
        this.swipeRefresh.apply {
            if (isRefreshing) {
                isRefreshing = !isRefreshing
            }
        }
    }

    private class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            handler.post(command)
        }

    }

}
