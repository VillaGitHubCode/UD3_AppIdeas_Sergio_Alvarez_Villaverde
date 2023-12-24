package utad.ud3_appideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import utad.ud3_appideas.room.models.Detail
import utad.ud3_appideas.room.models.Idea
import utad.ud3_appideas.room.models.IdeaAndDetailRelation

@Dao
interface IdeaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIdea(idea: Idea)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetail(detail: Detail)

    @Update
    fun updateIdea(idea: Idea)

    @Query("SELECT * FROM Idea")
    suspend fun getAllIdeas(): List<Idea>

    @Query("DELETE FROM Detail WHERE ideaId = :id")
    suspend fun deleteDetailByIdeaID(id: Int)


 //   @Query("SELECT * FROM Idea WHERE id=:idIdea")
 //   fun getIdea(idIdea:Int): Flow<Idea>

    @Transaction
    @Query("SELECT * FROM Idea WHERE id = :idIdea")
    fun getIdea(idIdea:Int): Flow<IdeaAndDetailRelation>

    @Delete
    fun deleteIdea(idea: Idea)



}