package com.grayseal.travelhub.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.grayseal.travelhub.utils.isEmailValid
import com.grayseal.travelhub.utils.isPasswordValid

/**
 * ViewModel class for handling user registration.
 */
class RegisterViewModel : ViewModel() {

    /**
     * Registers a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return LiveData containing the result of the registration process.
     */
    fun registerUser(email: String, password: String): LiveData<RegistrationResult> {
        // Create a LiveData instance to hold the registration result.
        val resultLiveData = MutableLiveData<RegistrationResult>()

        // Validate the email address.
        if (!isEmailValid(email)) {
            resultLiveData.value = RegistrationResult.InvalidEmail
            return resultLiveData
        }

        // Validate the password.
        if (!isPasswordValid(password)) {
            resultLiveData.value = RegistrationResult.InvalidPassword
            return resultLiveData
        }

        // Register the user using Firebase Authentication.
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Check if the registration was successful.
                if (task.isSuccessful) {
                    resultLiveData.value = RegistrationResult.Success
                } else {
                    // If registration failed, provide the error message.
                    resultLiveData.value = RegistrationResult.Failure(task.exception?.message)
                }
            }

        return resultLiveData
    }
}

/**
 * Sealed class representing the possible results of user registration.
 */
sealed class RegistrationResult {
    data object Success : RegistrationResult()
    data object InvalidEmail : RegistrationResult()
    data object InvalidPassword : RegistrationResult()
    data class Failure(val error: String?) : RegistrationResult()
}