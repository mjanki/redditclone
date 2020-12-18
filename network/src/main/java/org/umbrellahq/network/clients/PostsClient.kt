package org.umbrellahq.network.clients

import org.umbrellahq.network.models.PostResponseNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsClient {

    @GET("r/{term}/top.json?raw_json=1")
    suspend fun getPosts(@Path("term") term: String): Response<PostResponseNetworkEntity>
}