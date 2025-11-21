package com.example.keywordlogger

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun ParentModeScreen() {
    val db = FirebaseFirestore.getInstance()
    var childId by remember { mutableStateOf("") }
    var parentPassword by remember { mutableStateOf("") }
    var logs by remember { mutableStateOf(listOf<String>()) }
    var status by remember { mutableStateOf("") }

    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val correctPassword = "Anand*121#"   // ✅ Hardcoded parent password

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Parent Mode", style = MaterialTheme.typography.headlineMedium)

        // ✅ Input box for child ID
        OutlinedTextField(
            value = childId,
            onValueChange = { childId = it },
            label = { Text("Child ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ✅ Input box for parent password
        OutlinedTextField(
            value = parentPassword,
            onValueChange = { parentPassword = it },
            label = { Text("Parent Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation() // hides text
        )

        // ✅ Fetch button with password check
        Button(
            onClick = {
                if (childId.isBlank()) {
                    status = "⚠️ Please enter a child ID"
                } else if (parentPassword != correctPassword) {
                    status = "❌ Incorrect parent password"
                } else {
                    db.collection("children")
                        .document(childId)
                        .collection("logs")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { result ->
                            logs = result.documents.map { doc ->
                                val word = doc.getString("word") ?: ""
                                val time = doc.getLong("timestamp") ?: 0L
                                val formattedTime = formatter.format(Date(time))
                                "$word (at $formattedTime)"
                            }
                            status = "✅ Fetched ${logs.size} logs for $childId"
                        }
                        .addOnFailureListener { e ->
                            status = "❌ Error: ${e.message}"
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Logs")
        }

        if (status.isNotBlank()) {
            Text(status, style = MaterialTheme.typography.bodyMedium)
        }

        // ✅ Updated divider (HorizontalDivider instead of deprecated Divider)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(logs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        log,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
