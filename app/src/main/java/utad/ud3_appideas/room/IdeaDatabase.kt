package utad.ud3_appideas.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import utad.ud3_appideas.room.models.Detail
import utad.ud3_appideas.room.models.Idea

@Database(entities = [Idea::class, Detail::class], version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverters::class)
abstract class IdeaDatabase: RoomDatabase() {
    abstract fun ideaDao(): IdeaDAO
}



