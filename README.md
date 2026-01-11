# KeyWatch 📱

KeyWatch is an Android application built to explore **parental monitoring concepts**
using Android Accessibility Services, local buffering, and Firebase Firestore.

> ⚠️ Note: This project is for **educational and learning purposes**.
> Android restricts real-time keylogging on modern versions for privacy reasons.

---

## ✨ Features

### 👶 Child Mode
- Enable monitoring using Accessibility Service
- Capture visible on-screen text (non-keyboard)
- Track:
  - App package names
  - Screen content (where permitted)
- Logs are buffered locally using Room
- Background uploads handled via WorkManager

### 👨‍👩‍👧 Parent Mode
- Secure parent password using EncryptedSharedPreferences
- Fetch logs from Firebase Firestore
- View recent activity with timestamps and source app

---

## 🏗️ Architecture

- **UI**: Jetpack Compose
- **Background Tasks**: WorkManager
- **Local Storage**: Room Database
- **Remote Storage**: Firebase Firestore
- **Security**: EncryptedSharedPreferences
- **Monitoring**: AccessibilityService

---

## 🔐 Privacy & Platform Limitations

Android restricts access to:
- Keyboard input (IME / Gboard)
- Password fields
- Secure text fields

KeyWatch **does not bypass Android security mechanisms** and respects platform rules.

---

## 🔧 Firebase Setup

1. Create a Firebase project
2. Add Android app with package name:
3. Download `google-services.json`
4. Place it in:
5. Enable Firestore in Test Mode

⚠️ Do not commit `google-services.json` to GitHub.

---

## 🚀 Running the App

1. Clone the repository
2. Open in Android Studio
3. Add `google-services.json`
4. Build & Run
5. Enable Accessibility:

---

## 📌 Disclaimer

This project is intended for **learning and demonstration purposes only**.
It should not be used to violate user privacy or platform policies.

---

## 👤 Author

**Anand Chaudhary**  
Android Developer | Java / Kotlin | Firebase

Firebase Setup (Correct & Minimal)

You already have Firebase SDK in code. Now make sure Firebase Console is configured properly.

🔹 Step 1: Create Firebase Project

Go to 👉 https://console.firebase.google.com

Click Add project

Project name: KeyWatch

Disable Google Analytics (optional)

Create project

🔹 Step 2: Add Android App to Firebase

Inside Firebase project → Add app → Android

Package name (must match exactly):

com.example.keywordlogger


App nickname: KeyWatch

SHA-1 → Skip for now

Download google-services.json

⚠️ DO NOT upload this file to GitHub

Place it here locally:

app/google-services.json

🔹 Step 3: Enable Firestore

Firebase Console → Firestore Database

Click Create database

Select Test mode (for development)

Choose location (default is fine)

You already added rules — that’s okay for now.

🔹 Step 4: Verify Firestore Structure

When app works, data will look like:

children (collection)
 └── childId123 (document)
      └── logs (collection)
           ├── 1712345678901
           │    ├── word: "hello"
           │    ├── timestamp: 1712345678901
           │    ├── sourcePackage: "com.whatsapp"


If you see this → Firebase is correct ✅

🔹 Step 5: Protect Secrets (IMPORTANT)

Make sure .gitignore contains:

/app/google-services.json
/.idea/
/build/
/app/release/


If you already pushed google-services.json once:

git rm --cached app/google-services.json
git commit -m "remove google-services.json from repo"
git push

<img width="1872" height="778" alt="image" src="https://github.com/user-attachments/assets/66fa8c20-29ba-4395-8bce-7f1a1b186c44" />
