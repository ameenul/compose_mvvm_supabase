package com.example.cobasupabase.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseHolder {
    // Ganti dengan URL & anon/public key Supabase Anda
    private const val SUPABASE_URL = "https://apmlejbbadrnptnqohbz.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFwbWxlamJiYWRybnB0bnFvaGJ6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQzNDA1MDYsImV4cCI6MjA3OTkxNjUwNn0.rcQS3yAsSIMLOlhcwTTP5EY3CQWIYhSa3Nkq3JYfsBI"


    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest.Companion)
        install(Storage.Companion)
    }

    fun session(): UserSession? = client.auth.currentSessionOrNull()
}


