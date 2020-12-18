package org.umbrellahq.repository.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.umbrellahq.database.daos.PostDatabaseDao
import org.umbrellahq.database.models.PostDatabaseEntity
import org.umbrellahq.network.daos.PostsNetworkDao
import org.umbrellahq.repository.mappers.ErrorNetworkRepoNetworkMapper
import org.umbrellahq.repository.mappers.PostRepoDatabaseMapper
import org.umbrellahq.repository.mappers.PostRepoNetworkMapper
import org.umbrellahq.repository.models.PostRepoEntity

class PostsRepository(ctx: Context? = null) : ErrorRepository(ctx) {
    // DAOs
    private lateinit var postsNetworkDao: PostsNetworkDao
    private lateinit var postDatabaseDao: PostDatabaseDao

    // Observables
    private lateinit var allPosts: Flow<List<PostDatabaseEntity>>
    lateinit var isRetrievingPostsFlow: StateFlow<Boolean>

    // Mappers
    private val postRepoNetworkMapper = PostRepoNetworkMapper()
    private val postRepoDatabaseMapper = PostRepoDatabaseMapper()
    private var errorNetworkRepoNetworkMapper = ErrorNetworkRepoNetworkMapper()

    override fun init() {
        super.init()

        init(testPostsNetworkDao = null, testPostDatabaseDao = null)
    }

    fun init(testPostsNetworkDao: PostsNetworkDao? = null, testPostDatabaseDao: PostDatabaseDao? = null) {
        postsNetworkDao = testPostsNetworkDao ?: PostsNetworkDao()
        postDatabaseDao = testPostDatabaseDao ?: appDatabase.postDao()

        allPosts = postDatabaseDao.getAll()
        isRetrievingPostsFlow = postsNetworkDao.getIsRetrievingPostsFlow()

        GlobalScope.launch(Dispatchers.IO) {
            postsNetworkDao.getErrorNetworkChannel().collect { errorNetworkEntity ->
                val errorRepoEntity = errorNetworkRepoNetworkMapper.upstream(errorNetworkEntity)
                insertErrorNetwork(errorRepoEntity)
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            postsNetworkDao.getRetrievedPostsFlow().collectLatest {
                val postRepoList = it.map { postNetwork ->
                    postRepoNetworkMapper.upstream(postNetwork)
                }

                postDatabaseDao.insert(*postRepoList.map { postRepo ->
                    postRepoDatabaseMapper.downstream(postRepo)
                }.toTypedArray())
            }
        }
    }

    fun getPosts(): Flow<List<PostRepoEntity>> =
            allPosts.map { postDatabaseList ->
                postDatabaseList.map { postDatabase ->
                    postRepoDatabaseMapper.upstream(
                            postDatabase
                    )
                }
            }

    suspend fun getPostById(id: String): PostRepoEntity =
            postRepoDatabaseMapper.upstream(postDatabaseDao.getPostById(id))

    suspend fun retrieveReddit(searchTerm: String) {
        postDatabaseDao.deleteAll()
        postsNetworkDao.retrievePosts(searchTerm)
    }
}