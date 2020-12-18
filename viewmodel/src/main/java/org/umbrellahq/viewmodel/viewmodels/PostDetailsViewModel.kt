package org.umbrellahq.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.umbrellahq.repository.repositories.PostsRepository
import org.umbrellahq.viewmodel.mappers.PostViewModelRepoMapper
import org.umbrellahq.viewmodel.models.PostViewModelEntity

class PostDetailsViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var postsRepository: PostsRepository

    private val postLiveData = MutableLiveData<PostViewModelEntity>()
    fun getPostLiveData(): LiveData<PostViewModelEntity> = postLiveData

    private val postViewModelRepoMapper = PostViewModelRepoMapper()

    fun init(postId: String) {
        init(postId = postId, testPostsRepository = null)
    }

    fun init(postId: String, testPostsRepository: PostsRepository? = null) {
        postsRepository = testPostsRepository ?: PostsRepository(getApplication())
        postsRepository.init()

        viewModelScope.launch(Dispatchers.IO) {
            postLiveData.postValue(postViewModelRepoMapper.upstream(postsRepository.getPostById(postId)))
        }
    }
}