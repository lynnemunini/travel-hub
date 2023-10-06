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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.grayseal.travelhub.R
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.onboarding.viewmodel.SignInResult
import com.grayseal.travelhub.ui.onboarding.viewmodel.SignInViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var emailAddressEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var submitButton: MaterialButton
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
        progressBar = findViewById(R.id.signin_progress_bar)
        signupOptionTextView = findViewById(R.id.register_option_text_view)

        submitButton.setOnClickListener {
            val email = emailAddressEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Show the progress bar
            progressBar.visibility = View.VISIBLE

            viewModel.signInUser(email, password).observe(this) { result ->
                // Hide the progress bar when the sign-in result is observed
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
                        Toast.makeText(this, "Sign-in failed: ${result.error}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        signupOptionTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        signupOptionTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}