package com.grayseal.travelhub.utils

/**
 * Checks if the given email address is valid based on a regular expression pattern.
 *
 * @param email The email address to validate.
 * @return True if the email is valid, false otherwise.
 */
fun isEmailValid(email: String): Boolean {
    val regexPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(Regex(regexPattern))
}

/**
 * Checks if the given password is valid based on a length criterion.
 *
 * @param password The password to validate.
 * @return True if the password is valid, false otherwise.
 */
fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}