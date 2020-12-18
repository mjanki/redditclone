package org.umbrellahq.network.models

data class PostResponseNetworkEntity(
        var kind: String,
        var data: PostChildrenNetworkEntity
)