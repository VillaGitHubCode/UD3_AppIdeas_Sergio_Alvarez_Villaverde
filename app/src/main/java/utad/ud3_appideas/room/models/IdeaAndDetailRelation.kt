package utad.ud3_appideas.room.models

import androidx.room.Embedded
import androidx.room.Relation

data class IdeaAndDetailRelation(
    @Embedded
    val idea: Idea,
    @Relation(
        parentColumn = "id",
        entityColumn = "ideaId"
    )
    val detailList: List<Detail>?
)
