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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.grayseal.travelhub.R
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.onboarding.viewmodel.RegisterViewModel
import com.grayseal.travelhub.ui.onboarding.viewmodel.RegistrationResult

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var emailAddressEditText: EditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var submitButton: MaterialButton
    private lateinit var signInTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
        progressBar = findViewById(R.id.register_progress_bar)
        signInTextView = findViewById(R.id.register_sign_in_option_text_view)

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
                        // Show error message for invalid email
                        Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_LONG)
                            .show()
                    }

                    is RegistrationResult.InvalidPassword -> {
                        // Show error message for invalid password
                        Toast.makeText(
                            applicationContext,
                            "Password must be atleast 6 characters long",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is RegistrationResult.Failure -> {
                        // Registration failed, show an error message to the user
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
}