package com.grayseal.travelhub.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.grayseal.travelhub.utils.isEmailValid
import com.grayseal.travelhub.utils.isPasswordValid

class RegisterViewModel : ViewModel() {

    /**
     * Registers a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return LiveData containing the result of the registration process.
     */
    fun registerUser(email: String, password: String): LiveData<RegistrationResult> {
        // LiveData instance to hold the registration result
        val resultLiveData = MutableLiveData<RegistrationResult>()

        if (!isEmailValid(email)) {
            resultLiveData.value = RegistrationResult.InvalidEmail
            return resultLiveData
        }

        if (!isPasswordValid(password)) {
            resultLiveData.value = RegistrationResult.InvalidPassword
            return resultLiveData
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.value = RegistrationResult.Success
                } else {
                    resultLiveData.value = RegistrationResult.Failure(task.exception?.message)
                }
            }

        return resultLiveData
    }
}

sealed class RegistrationResult {
    data object Success : RegistrationResult()
    data object InvalidEmail : RegistrationResult()
    data object InvalidPassword : RegistrationResult()
    data class Failure(val error: String?) : RegistrationResult()
}