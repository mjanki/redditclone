package org.umbrellahq.network.models

data class PostChildDataNetworkEntity(
        var id: String,
        var title: String,
        var thumbnail: String?,
        var author_fullname: String
)