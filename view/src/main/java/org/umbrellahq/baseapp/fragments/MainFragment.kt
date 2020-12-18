package org.umbrellahq.baseapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import org.umbrellahq.baseapp.R
import org.umbrellahq.baseapp.adapters.PostsRecyclerViewAdapter
import org.umbrellahq.baseapp.mappers.PostViewViewModelMapper
import org.umbrellahq.viewmodel.viewmodels.PostsViewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var postsVM: PostsViewModel

    private val postsRecyclerViewAdapter = PostsRecyclerViewAdapter(arrayOf())

    private val postViewViewModelMapper = PostViewViewModelMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postsVM = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsVM.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bSearch.setOnClickListener {
            postsVM.retrievePosts("${etSearch.text}")

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(clMainFragment.windowToken, 0)
        }

        setupPostsRecyclerView()
        setupPostsObservers()
    }

    private fun setupPostsRecyclerView() {
        recyclerView.adapter = postsRecyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        postsRecyclerViewAdapter.onClick = {
            findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailsFragment(it.id)
            )
        }
    }

    private fun setupPostsObservers() {
        postsVM.allPosts.observe(viewLifecycleOwner, {
            postsRecyclerViewAdapter.posts = it.map { postViewModelEntity ->
                postViewViewModelMapper.upstream(postViewModelEntity)
            }.toTypedArray()

            postsRecyclerViewAdapter.notifyDataSetChanged()
        })

        postsVM.getIsRetrievingPosts().observe(viewLifecycleOwner, {
            compLoading.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }
}