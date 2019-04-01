package com.frivan.tools

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frivan.tools.adapter.allsorts.ContentData
import com.frivan.tools.adapter.base.ItemData
import com.frivan.tools.data.FakeContentStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_loading.view.*
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

        fab.setOnClickListener { view ->
            this.initView(savedInstanceState)
        }

        this.initView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()?.let {
            outState.putInt(EXTRA_POSITION, it)
            Log.d(this.javaClass.name, "onSaveInstanceState=$it")
        }

        super.onSaveInstanceState(outState)
    }

    private fun initView(savedInstanceState: Bundle?) {

        //region Paged AllKindsAdapter

        val addapter = com.frivan.tools.adapter.allsorts.paged.AllKindsAdapter()

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
            recyclerView.addItemDecoration(loadingItemDecoration)
        }, {
            recyclerView.removeItemDecoration(loadingItemDecoration)
            this.endUpdate()
        })

        val pagedList = PagedList.Builder(itemDataSource, config)
            .setFetchExecutor(MainThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .also {
                savedInstanceState?.getInt(EXTRA_POSITION)?.let { position ->
                    Log.d(this.javaClass.name, "setInitialKey=$position")
                    it.setInitialKey(position)
                }
            }
            .build()

        addapter.submitList(pagedList)

        this.recyclerView.adapter = addapter

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

        //region decorationView settings

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
                    swipeRefresh.isEnabled = true
                } else if (swipeRefresh.isEnabled && !swipeRefresh.isRefreshing) {
                    swipeRefresh.isEnabled = false
                }
            }
        })

        //endregion decorationView settings
    }

    private fun endUpdate() {
        this.swipeRefresh.apply {
            if (isRefreshing) {
                isRefreshing = !isRefreshing
            }
        }
    }

    //region for paging

    private class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            handler.post(command)
        }
    }

    private class ItemDataSource(
        private val loadingCallback: (() -> Unit)? = null,
        private val endLoadingCallback: (() -> Unit)? = null,
        private val contentStorage: FakeContentStorage = FakeContentStorage()
    ) :
        PositionalDataSource<ItemData>() {

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ItemData>) {
            loadingCallback?.invoke()

            contentStorage.getData(params.startPosition, params.loadSize) { result ->
                endLoadingCallback?.invoke()

                callback.onResult(result.list.map {
                    ContentData(it)
                })
            }
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ItemData>) {
            contentStorage.getData(params.requestedStartPosition, params.requestedLoadSize) { result ->
                val position = if (result.list.isEmpty()) {
                    0
                } else {
                    params.requestedStartPosition
                }

                val list = result.list.map {
                    ContentData(it)
                }

                if (params.placeholdersEnabled) {
                    callback.onResult(list, position, result.count)
                } else {
                    callback.onResult(list, position)
                }

                Log.d(this.javaClass.name, "loadInitial=${params.requestedStartPosition}")
            }
        }
    }

    private class ItemDataSourceFactory : DataSource.Factory<Int, ItemData>() {

        override fun create(): DataSource<Int, ItemData> {
            return ItemDataSource()
        }

    }

    private class LoadingItemDecoration : RecyclerView.ItemDecoration() {

        var decorationView: View? = null

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position == parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = getDecorationView(parent)?.measuredHeight ?: outRect.bottom
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)

            for (i in 0..parent.childCount) {
                val child = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(child)

                if (position == parent.adapter?.itemCount?.minus(1)) {
                    getDecorationView(parent)?.let { decorationView ->
                        decorationView.y = child.bottom.toFloat()
                        parent.drawChild(c, decorationView, System.currentTimeMillis())

                        ViewCompat.postInvalidateOnAnimation(parent)
                        decorationView.progressBar?.invalidate()
                    }
                }
            }
        }

        fun getDecorationView(parent: ViewGroup): View? {
            return if (decorationView == null) {
                decorationView = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)

                val widthSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.width,
                    View.MeasureSpec.EXACTLY
                )
                val heightSpec = View.MeasureSpec.makeMeasureSpec(
                    parent.height,
                    View.MeasureSpec.UNSPECIFIED
                )

                decorationView?.measure(widthSpec, heightSpec)

                decorationView?.layout(0, 0, decorationView?.measuredWidth ?: 0, (decorationView?.measuredHeight ?: 0))

                decorationView
            } else {
                decorationView
            }
        }
    }

    //endregion for paging

}
