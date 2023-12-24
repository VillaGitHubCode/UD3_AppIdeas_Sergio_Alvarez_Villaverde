package utad.ud3_appideas.room.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Idea(
    @PrimaryKey(autoGenerate= true)
    val id: Int,
    val name: String,
    val priority: String,
    val progress: String,
    val description: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: Bitmap
)


