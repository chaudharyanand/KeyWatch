package com.example.keywordlogger

import android.app.Application
import com.google.firebase.FirebaseApp
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase once for the whole app
        FirebaseApp.initializeApp(this)
        monitorFirestoreConnection()
    }
    fun monitorFirestoreConnection() {
        val db = FirebaseFirestore.getInstance()

        // Try a dummy write to confirm connectivity
        db.collection("connectionTest")
            .add(mapOf("ping" to System.currentTimeMillis()))
            .addOnSuccessListener {
                Log.d("FirestoreConnection", "Firestore connected, write succeeded")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreConnection", " Firestore connection failed: ${e.message}")
            }
    }
}
