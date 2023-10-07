package com.grayseal.travelhub.ui.onboarding.view

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.grayseal.travelhub.R
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.onboarding.viewmodel.RegisterViewModel
import com.grayseal.travelhub.ui.onboarding.viewmodel.RegistrationResult

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var emailAddressEditText: EditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var submitButton: MaterialButton
    private lateinit var googleSignUpButton: MaterialButton
    private lateinit var signInTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // User is already authenticated, navigate to the MainDashboardActivity
            val intent = Intent(this, MainDashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_register_layout)
            viewModel = ViewModelProvider(this@RegisterActivity)[RegisterViewModel::class.java]
            initializeResources()
        }
    }

    private fun initializeResources() {
        emailAddressEditText = findViewById(R.id.register_email_address_edit_text)
        passwordEditText = findViewById(R.id.register_password_edit_text)
        submitButton = findViewById(R.id.register_submit_button)
        googleSignUpButton = findViewById(R.id.sign_up_google_button)
        progressBar = findViewById(R.id.register_progress_bar)
        signInTextView = findViewById(R.id.register_sign_in_option_text_view)

        googleSignUpButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            handleGoogleSignIn()
        }

        submitButton.setOnClickListener {
            val email = emailAddressEditText.text.toString()
            val password = passwordEditText.text.toString()
            progressBar.visibility = View.VISIBLE
            viewModel.registerUser(email, password).observe(this) { result ->
                progressBar.visibility = View.GONE
                when (result) {
                    is RegistrationResult.Success -> {
                        // Registration successful, handle navigation
                        val intent = Intent(this, MainDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is RegistrationResult.InvalidEmail -> {
                        Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_LONG)
                            .show()
                    }

                    is RegistrationResult.InvalidPassword -> {
                        Toast.makeText(
                            applicationContext,
                            "Password must be atleast 6 characters long",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is RegistrationResult.Failure -> {
                        Toast.makeText(
                            this,
                            "Registration failed: ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        signInTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(resources.getString(R.string.server_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    "Google Sign-In Failed: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            }

            task.addOnSuccessListener { googleSignInAccount ->
                // Google Sign-In was successful, authenticate the user in Firebase
                val googleIdToken = googleSignInAccount.idToken
                val googleDisplayName = googleSignInAccount.displayName
                val googleEmail = googleSignInAccount.email

                // Use the Google ID Token to authenticate with Firebase
                val credential = GoogleAuthProvider.getCredential(googleIdToken, null)

                // Authenticate with Firebase
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { authTask ->
                        if (authTask.isSuccessful) {
                            val intent = Intent(this, MainDashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Firebase Authentication Failed: ${authTask.exception?.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.GONE
                        }
                    }
            }
        }
    }

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 1211
    }
}