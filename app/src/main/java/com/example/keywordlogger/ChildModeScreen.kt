package com.example.keywordlogger

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ChildModeScreen() {
    val context = LocalContext.current
    var childId by remember { mutableStateOf("") }
    var loggingEnabled by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Child Mode", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = childId,
            onValueChange = { childId = it },
            label = { Text("Child ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (childId.isNotBlank()) {
                loggingEnabled = !loggingEnabled
                val prefs = context.getSharedPreferences("keyword_logger_prefs", Context.MODE_PRIVATE)
                val editor = prefs.edit()

                if (loggingEnabled) {
                    // ✅ Save both childId and loggingEnabled
                    editor.putString("child_id", childId)
                    editor.putBoolean("logging_enabled", true)
                    editor.apply()

                    status = "✅ Logging enabled for childId: $childId\nGo to Accessibility settings to activate service."
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } else {
                    // ⏹️ Disable logging
                    editor.putBoolean("logging_enabled", false)
                    editor.apply()

                    status = "⏹️ Logging disabled"
                }
                Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
            } else {
                status = "⚠️ Please enter a valid child ID"
            }
        }) {
            Text(if (loggingEnabled) "Disable Logging" else "Enable Logging")
        }

        if (status.isNotBlank()) {
            Text(status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
