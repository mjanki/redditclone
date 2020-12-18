package org.umbrellahq.viewmodel.mappers

import org.umbrellahq.repository.models.PostRepoEntity
import org.umbrellahq.util.interfaces.Mapper
import org.umbrellahq.viewmodel.models.PostViewModelEntity

class PostViewModelRepoMapper : Mapper<PostViewModelEntity, PostRepoEntity> {
    override fun downstream(currentLayerEntity: PostViewModelEntity) = PostRepoEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbnailUrl = currentLayerEntity.thumbnailUrl,
            author = currentLayerEntity.author
    )

    override fun upstream(nextLayerEntity: PostRepoEntity) = PostViewModelEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbnailUrl = nextLayerEntity.thumbnailUrl,
            author = nextLayerEntity.author
    )
}