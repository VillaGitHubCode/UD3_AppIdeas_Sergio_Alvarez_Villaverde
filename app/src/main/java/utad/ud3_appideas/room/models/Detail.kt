package utad.ud3_appideas.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Detail(
    @PrimaryKey(autoGenerate= true)
    val id : Int,
    val ideaId : Int,
    val detail : String
)
