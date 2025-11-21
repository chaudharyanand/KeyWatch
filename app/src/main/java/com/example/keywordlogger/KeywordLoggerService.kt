package com.example.keywordlogger

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class KeywordLoggerService : AccessibilityService() {

    private val db = FirebaseFirestore.getInstance()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Debug: confirm events are firing
        Log.d("KeywordLoggerService", "Event received: ${event.eventType}")

        // Always read prefs so service works even if app UI is closed
        val prefs = getSharedPreferences("keyword_logger_prefs", Context.MODE_PRIVATE)
        val childId = prefs.getString("child_id", null)
        val loggingEnabled = prefs.getBoolean("logging_enabled", false)

        if (!loggingEnabled) {
            Log.d("KeywordLoggerService", "Logging disabled, skipping")
            return
        }
        if (childId.isNullOrBlank()) {
            Log.e("KeywordLoggerService", "Child ID not set, skipping log")
            return
        }

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ||
            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            val text = event.text?.joinToString(" ") ?: return
            if (text.isBlank()) return

            val timestamp = System.currentTimeMillis()
            val log = mapOf(
                "childId" to childId,
                "word" to text,
                "timestamp" to timestamp
            )

            try {
                // ✅ Store under children/{childId}/logs/{timestamp}
                db.collection("children")
                    .document(childId)
                    .collection("logs")
                    .document(timestamp.toString())
                    .set(log)
                    .addOnSuccessListener {
                        Log.d("KeywordLoggerService", "✅ Logged '$text' for childId: $childId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("KeywordLoggerService", "❌ Error logging", e)
                    }
            } catch (e: Exception) {
                Log.e("KeywordLoggerService", "Service crashed while logging", e)
            }
        }
    }

    override fun onInterrupt() {
        Log.d("KeywordLoggerService", "Service interrupted")
    }
}
