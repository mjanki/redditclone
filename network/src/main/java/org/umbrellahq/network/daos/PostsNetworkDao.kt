package org.umbrellahq.network.daos

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.umbrellahq.network.clients.PostsClient
import org.umbrellahq.network.models.ErrorNetworkEntity
import org.umbrellahq.network.models.PostChildDataNetworkEntity
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PostsNetworkDao : BaseNetworkDao() {
    private var requestInterface: PostsClient = Retrofit.Builder()
            .baseUrl("https://reddit.com")
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build().create(PostsClient::class.java)

    fun setRequestInterface(taskClient: PostsClient) {
        requestInterface = taskClient
    }

    private val isRetrievingPostsFlow = MutableStateFlow(false)
    fun getIsRetrievingPostsFlow(): StateFlow<Boolean> = isRetrievingPostsFlow

    // TODO: replace with retrievedTasksChannel.asFlow() when it's out of preview
    private val retrievedPostsChannel = ConflatedBroadcastChannel<List<PostChildDataNetworkEntity>>()
    fun getRetrievedPostsFlow(): Flow<List<PostChildDataNetworkEntity>> = retrievedPostsChannel.openSubscription().receiveAsFlow()

    suspend fun retrievePosts(searchTerm: String) {
        isRetrievingPostsFlow.value = true

        val posts = executeNetworkCall(
                request = { requestInterface.getPosts(searchTerm) },
                action = "Fetching tasks from the cloud"
        )

        posts?.let {
            val filtered = it.data.children.map { child ->
                child.data
            }.filter { data ->
                data.thumbnail?.isNotEmpty() == true && data.thumbnail != "self" && data.thumbnail != "default"
            }

            if (filtered.isEmpty()) {
                errorNetworkChannel.send(ErrorNetworkEntity(message = "No Posts Found!"))
            } else {
                retrievedPostsChannel.send(filtered)
            }
        }

        isRetrievingPostsFlow.value = false
    }
}