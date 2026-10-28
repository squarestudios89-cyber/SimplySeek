package com.squarestudios.simplyseek.leaderboard

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository class managing Firebase Firestore global leaderboards.
 */
class LeaderboardRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {

    private val leaderboardCollection = firestore.collection("leaderboards")

    /**
     * Submits a player's puzzle completion time to the global leaderboard.
     *
     * @param playerName Display name of the player.
     * @param timeInSeconds Completion time in seconds.
     * @param difficulty Difficulty tier (e.g., "Easy", "Medium", "Hard").
     * @return [Result] indicating success or failure.
     */
    suspend fun submitScore(
        playerName: String,
        timeInSeconds: Long,
        difficulty: String,
    ): Result<Unit> {
        return try {
            val entry = hashMapOf(
                "playerName" to playerName,
                "timeInSeconds" to timeInSeconds,
                "difficulty" to difficulty,
                "timestamp" to System.currentTimeMillis(),
            )
            leaderboardCollection.add(entry).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Listens in real time to the top fastest times (lowest time first) for a specific [difficulty].
     *
     * @param difficulty Difficulty filter ("Easy", "Medium", "Hard").
     * @param limit Maximum number of records to retrieve (default: 50).
     * @return [Flow] emitting the updated list of top [LeaderboardEntry] items.
     */
    fun getTopScoresFlow(
        difficulty: String,
        limit: Long = 50L,
    ): Flow<List<LeaderboardEntry>> = callbackFlow {
        val query = leaderboardCollection
            .whereEqualTo("difficulty", difficulty)
            .orderBy("timeInSeconds", Query.Direction.ASCENDING)
            .limit(limit)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val entries = snapshot.toObjects(LeaderboardEntry::class.java)
                trySend(entries)
            }
        }

        awaitClose {
            registration.remove()
        }
    }
}
