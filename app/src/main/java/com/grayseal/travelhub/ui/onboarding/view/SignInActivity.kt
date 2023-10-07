package com.grayseal.travelhub.ui.onboarding.view

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
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
import com.grayseal.travelhub.ui.onboarding.view.RegisterActivity.Companion.GOOGLE_SIGN_IN_REQUEST_CODE
import com.grayseal.travelhub.ui.onboarding.viewmodel.SignInResult
import com.grayseal.travelhub.ui.onboarding.viewmodel.SignInViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var emailAddressEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var submitButton: MaterialButton
    private lateinit var googleSignInButton: MaterialButton
    private lateinit var signupOptionTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_layout)
        viewModel = ViewModelProvider(this@SignInActivity)[SignInViewModel::class.java]
        initializeResources()
    }

    private fun initializeResources() {
        emailAddressEditText = findViewById(R.id.signin_email_address_edit_text)
        passwordEditText = findViewById(R.id.signin_password_edit_text)
        submitButton = findViewById(R.id.signin_submit_button)
        googleSignInButton = findViewById(R.id.sign_in_google_button)
        progressBar = findViewById(R.id.signin_progress_bar)
        signupOptionTextView = findViewById(R.id.register_option_text_view)

        submitButton.setOnClickListener {
            val email = emailAddressEditText.text.toString()
            val password = passwordEditText.text.toString()

            progressBar.visibility = View.VISIBLE

            viewModel.signInUser(email, password).observe(this) { result ->
                progressBar.visibility = View.GONE

                when (result) {
                    is SignInResult.Success -> {
                        // Sign-in successful, handle navigation to main dashboard
                        val intent = Intent(this, MainDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is SignInResult.Failure -> {
                        // Sign-in failed, show an error message to the user
                        Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        googleSignInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            handleGoogleSignIn()
        }

        signupOptionTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        signupOptionTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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
            task.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.authentication_failed_error_msg),
                    Toast.LENGTH_LONG
                )
                    .show()
                progressBar.visibility = View.GONE
            }

            task.addOnSuccessListener { googleSignInAccount ->
                // Google Sign-In was successful, authenticate the user in Firebase
                val googleIdToken = googleSignInAccount.idToken
                val googleDisplayName = googleSignInAccount.displayName
                val googleEmail = googleSignInAccount.email

                // Use the Google ID Token to authenticate with Firebase
                val credential = GoogleAuthProvider.getCredential(googleIdToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { authTask ->
                        if (authTask.isSuccessful) {
                            // Firebase Authentication successful, navigate to the main dashboard
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}