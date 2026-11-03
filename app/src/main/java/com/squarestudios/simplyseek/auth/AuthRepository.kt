package com.squarestudios.simplyseek.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.squarestudios.simplyseek.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository managing Firebase Google Authentication and player account sessions.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    val isSignedIn: Boolean
        get() = _currentUser.value != null

    val playerName: String
        get() = _currentUser.value?.displayName?.ifBlank { null }
            ?: _currentUser.value?.email?.substringBefore('@')
            ?: "Guest Seeker"

    val photoUrl: String?
        get() = _currentUser.value?.photoUrl?.toString()

    val userId: String?
        get() = _currentUser.value?.uid

    /**
     * Initiates Google Sign-In via Android Credential Manager and signs in to Firebase.
     *
     * @param context Active Android Activity Context required by Credential Manager.
     */
    suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
        return try {
            val credentialManager = CredentialManager.create(context)

            val serverClientId = try {
                context.getString(R.string.default_web_client_id)
            } catch (_: Exception) {
                // Fallback client ID from google-services.json
                "402042129809-u1jeuo4l64ntv7oftchdvl7oevddibcs.apps.googleusercontent.com"
            }

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context = context, request = request)
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            val authResult = auth.signInWithCredential(authCredential).await()

            val user = authResult.user
            if (user != null) {
                _currentUser.value = user
                Result.success(user)
            } else {
                Result.failure(Exception("Failed to retrieve signed-in user."))
            }
        } catch (_: GetCredentialCancellationException) {
            // User cancelled Google Sign-In dialog
            Result.failure(Exception("Google Sign-In was cancelled."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs out the current player from Firebase.
     */
    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }
}
