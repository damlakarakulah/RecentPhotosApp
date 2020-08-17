package com.example.recentphotosapp.activity

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recentphotosapp.R
import com.example.recentphotosapp.adapter.ImageGridAdapter
import com.example.recentphotosapp.model.Photo
import com.example.recentphotosapp.task.AsyncResponse
import com.example.recentphotosapp.task.PhotoTask
import com.example.recentphotosapp.util.Config
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var layoutManager: StaggeredGridLayoutManager? = null
    var photos: MutableList<Photo>? = null
    var imageList = ArrayList<String>()
    var transaction: FragmentTransaction? = null
    var newFragment: Fragment? = null

    companion object {
        var Instance: MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Instance = this
        viewLayout.visibility = View.GONE
        photoTask()

    }

    private fun photoTask() {
        val photoTask = PhotoTask(this, object : AsyncResponse {
            override fun processFinish(output: Any?) {
                setLayoutManager()
                refreshList()
                addPhotos()
                setAdapter()
                setSnapHelper()
                rv.adapter!!.notifyDataSetChanged()
            }
        })
        photoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }


    private fun setLayoutManager() {
        if (layoutManager == null) {
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rv.layoutManager = layoutManager
        }
    }

    private fun refreshList() {
        if (imageList.size == 0)
            imageList = ArrayList<String>()
    }

    private fun addPhotos() {
        photos = Config.photos
        for (i in 0 until photos!!.size)
            imageList.add(photos!![i].getUrl())
    }

    private fun setAdapter() {
        if (rv.adapter != null)
            rv.adapter!!.notifyDataSetChanged()
        else {
            val imageGridAdapter = ImageGridAdapter(applicationContext, imageList)
            rv.adapter = imageGridAdapter
            addOnScrollListener()
        }
    }

    private fun addOnScrollListener() {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (ImageGridAdapter.end) {
                        photoTask()
                    }
                }
            }
        })
    }

    private fun setSnapHelper() {
        if (rv.onFlingListener != null) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rv)
        }
    }

    fun changeFragment(
        fragment: Fragment,
        tag: String,
        backstack: String?,
        bundle: Bundle?
    ): Fragment? {
        transaction = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        newFragment = fragment
        if (supportFragmentManager.findFragmentByTag(tag) != null) {
            val currentFragment: Fragment = supportFragmentManager.findFragmentByTag(tag)!!
            if (currentFragment.lifecycle.currentState == Lifecycle.State.INITIALIZED)
                transaction!!.remove(currentFragment)
        }
        if (bundle != null)
            newFragment!!.arguments = bundle
        transaction!!.replace(R.id.viewLayout, newFragment!!, tag)
        transaction!!.addToBackStack(backstack)
        transaction!!.commitAllowingStateLoss()

        return newFragment
    }

    override fun onBackPressed() {
        val count: Int = Instance!!.supportFragmentManager.backStackEntryCount
        if (count == 0) {
            Instance!!.finish()
        } else {
            Instance!!.supportFragmentManager.popBackStack()
            viewLayout.visibility = View.GONE
        }
    }

}
