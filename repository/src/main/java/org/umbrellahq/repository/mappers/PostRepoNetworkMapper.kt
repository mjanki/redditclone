package org.umbrellahq.repository.mappers

import org.umbrellahq.network.models.PostChildDataNetworkEntity
import org.umbrellahq.repository.models.PostRepoEntity
import org.umbrellahq.util.interfaces.Mapper

class PostRepoNetworkMapper : Mapper<PostRepoEntity, PostChildDataNetworkEntity> {
    override fun downstream(currentLayerEntity: PostRepoEntity) = PostChildDataNetworkEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbnail = currentLayerEntity.thumbnailUrl,
            author_fullname = currentLayerEntity.author
    )

    override fun upstream(nextLayerEntity: PostChildDataNetworkEntity) = PostRepoEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbnailUrl = nextLayerEntity.thumbnail ?: "N/A",
            author = nextLayerEntity.author_fullname
    )
}