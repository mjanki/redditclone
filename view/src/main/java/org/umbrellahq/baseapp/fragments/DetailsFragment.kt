package org.umbrellahq.baseapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*
import org.umbrellahq.baseapp.R
import org.umbrellahq.viewmodel.viewmodels.PostDetailsViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var postDetailsVM: PostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postDetailsVM = ViewModelProviders.of(this).get(PostDetailsViewModel::class.java)
        postDetailsVM.init(args.postId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postDetailsVM.getPostLiveData().observe(viewLifecycleOwner, {
            tvTitle.text = it.title

            Glide.with(this).load(it.thumbnailUrl).centerCrop().into(ivImage)
        })
    }
}