package com.kreggscode.hebrewverbs.data.database

import androidx.room.*
import com.kreggscode.hebrewverbs.data.model.Binyan
import com.kreggscode.hebrewverbs.data.model.HebrewVerb
import kotlinx.coroutines.flow.Flow

@Dao
interface VerbDao {
    @Query("SELECT * FROM verbs ORDER BY frequencyRank ASC")
    fun getAllVerbs(): Flow<List<HebrewVerb>>

    @Query("SELECT * FROM verbs WHERE binyan = :binyan ORDER BY frequencyRank ASC")
    fun getVerbsByBinyan(binyan: Binyan): Flow<List<HebrewVerb>>

    @Query("SELECT * FROM verbs WHERE isFavorite = 1 ORDER BY frequencyRank ASC")
    fun getFavoriteVerbs(): Flow<List<HebrewVerb>>

    @Query("SELECT * FROM verbs WHERE id = :id")
    suspend fun getVerbById(id: Int): HebrewVerb?

    @Query("SELECT * FROM verbs WHERE hebrew LIKE '%' || :query || '%' OR transliteration LIKE '%' || :query || '%' OR english LIKE '%' || :query || '%'")
    fun searchVerbs(query: String): Flow<List<HebrewVerb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerb(verb: HebrewVerb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerbs(verbs: List<HebrewVerb>)

    @Update
    suspend fun updateVerb(verb: HebrewVerb)

    @Delete
    suspend fun deleteVerb(verb: HebrewVerb)

    @Query("UPDATE verbs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}
