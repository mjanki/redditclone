package org.umbrellahq.util.model

data class PostDataEntity(
        var id: String,
        var title: String,
        var thumbnail: String?,
        var author_fullname: String,
        var status: Int
)