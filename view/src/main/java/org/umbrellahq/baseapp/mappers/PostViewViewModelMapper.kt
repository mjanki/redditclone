package org.umbrellahq.baseapp.mappers

import org.umbrellahq.baseapp.models.PostViewEntity
import org.umbrellahq.util.interfaces.Mapper
import org.umbrellahq.viewmodel.models.PostViewModelEntity

class PostViewViewModelMapper : Mapper<PostViewEntity, PostViewModelEntity> {
    override fun downstream(currentLayerEntity: PostViewEntity) = PostViewModelEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbnailUrl = currentLayerEntity.thumbnailUrl,
            author = currentLayerEntity.author
    )

    override fun upstream(nextLayerEntity: PostViewModelEntity) = PostViewEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbnailUrl = nextLayerEntity.thumbnailUrl,
            author = nextLayerEntity.author
    )
}