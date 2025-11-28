package com.example.cobasupabase.domain.repositories




import com.example.cobasupabase.data.remote.SupabaseHolder
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserSession

class AuthRepository {
    private val auth: Auth get() = SupabaseHolder.client.auth

    suspend fun register(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun logout() {
        auth.signOut()
    }

    fun currentSession(): UserSession? = SupabaseHolder.session()
}