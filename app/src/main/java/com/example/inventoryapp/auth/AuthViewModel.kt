package com.example.inventoryapp.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }

    private fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun checkAuthState() {
        _authState.value = AuthState.Loading
        val user = auth.currentUser
        _authState.value = if (user != null) AuthState.Authenticated else AuthState.Idle
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            setError("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.localizedMessage ?: "Login failed")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            setError("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.localizedMessage ?: "Signup failed")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }
}



sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Idle : AuthState()
    data class Error(val message: String) : AuthState()
}
