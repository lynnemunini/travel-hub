package com.grayseal.travelhub.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel class for handling user sign-in.
 */
class SignInViewModel : ViewModel() {

    /**
     * Signs in a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return LiveData containing the result of the sign-in process.
     */
    fun signInUser(email: String, password: String): LiveData<SignInResult> {
        // Create a LiveData instance to hold the sign-in result.
        val resultLiveData = MutableLiveData<SignInResult>()

        // Sign-in using Firebase Authentication.
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Check if the sign-in was successful.
                if (task.isSuccessful) {
                    resultLiveData.value = SignInResult.Success
                } else {
                    // If sign-in failed, provide the error message.
                    resultLiveData.value = SignInResult.Failure(task.exception?.message)
                }
            }

        return resultLiveData
    }
}

/**
 * Sealed class representing the possible results of user sign-in.
 */
sealed class SignInResult {
    data object Success : SignInResult()
    data class Failure(val error: String?) : SignInResult()
}