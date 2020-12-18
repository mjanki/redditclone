package org.umbrellahq.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.umbrellahq.repository.repositories.PostsRepository
import org.umbrellahq.viewmodel.mappers.PostViewModelRepoMapper
import org.umbrellahq.viewmodel.models.PostViewModelEntity

class PostsViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var postsRepository: PostsRepository
    lateinit var allPosts: LiveData<List<PostViewModelEntity>>

    private val postViewModelRepoMapper = PostViewModelRepoMapper()

    fun init() {
        init(testPostsRepository = null)
    }

    fun init(testPostsRepository: PostsRepository? = null) {
        postsRepository = testPostsRepository ?: PostsRepository(getApplication())
        postsRepository.init()

        allPosts = liveData(Dispatchers.IO) {
            postsRepository.getPosts().collectLatest { postRepoList ->
                emit(postRepoList.map { postRepo ->
                    postViewModelRepoMapper.upstream(postRepo)
                })
            }
        }

        isRetrievingPosts = postsRepository.isRetrievingPostsFlow.asLiveData(Dispatchers.IO)
    }

    private lateinit var isRetrievingPosts: LiveData<Boolean>
    fun getIsRetrievingPosts(): LiveData<Boolean> = isRetrievingPosts

    fun retrievePosts(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            postsRepository.retrieveReddit(searchTerm)
        }
    }
}