package br.com.alertaja.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "occurrences")
data class Occurrence(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val neighborhood: String,
    val description: String,
    val severity: String,
    val status: String = "Pendente",
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface OccurrenceDao {
    @Query("SELECT * FROM occurrences ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Occurrence>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: Occurrence)

    @Query("DELETE FROM occurrences WHERE id = :id")
    suspend fun delete(id: Long)
}

@Database(entities = [Occurrence::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun occurrenceDao(): OccurrenceDao
}
