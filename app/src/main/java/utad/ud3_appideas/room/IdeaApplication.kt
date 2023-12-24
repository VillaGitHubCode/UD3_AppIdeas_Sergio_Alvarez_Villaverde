package utad.ud3_appideas.room

import android.app.Application
import androidx.room.Room

class IdeaApplication : Application() {

    lateinit var dataBase : IdeaDatabase

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(this, IdeaDatabase::class.java, "IdeaDatabase").build()
    }
}