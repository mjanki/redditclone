package org.umbrellahq.repository.mappers

import org.umbrellahq.database.models.PostDatabaseEntity
import org.umbrellahq.repository.models.PostRepoEntity
import org.umbrellahq.util.interfaces.Mapper

class PostRepoDatabaseMapper : Mapper<PostRepoEntity, PostDatabaseEntity> {
    override fun downstream(currentLayerEntity: PostRepoEntity) = PostDatabaseEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbnailUrl = currentLayerEntity.thumbnailUrl,
            author = currentLayerEntity.author
    )

    override fun upstream(nextLayerEntity: PostDatabaseEntity) = PostRepoEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbnailUrl = nextLayerEntity.thumbnailUrl,
            author = nextLayerEntity.author
    )
}