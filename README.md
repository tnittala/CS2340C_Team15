# WanderSync 🧭
**Collaborative Travel Management App** for organizing trips, reservations, and itineraries in real time with multiple users.

---

## 📖 Overview
WanderSync is an Android application designed to streamline **collaborative travel planning**. Users can coordinate trip details, make reservations, and view logistics in a centralized, user-friendly interface. Built with **real-time synchronization** and a **modular MVVM architecture**, WanderSync ensures seamless updates and collaboration across multiple devices.

---

## ✨ Key Features

- 📆 **Trip Itinerary Management** — Create and manage trip details including destinations, dates, and collaborators.  
- 🏨 **Reservation Tracking** — Add and view dining and accommodation reservations with times, websites, reviews, and room details.  
- 🧑‍🤝‍🧑 **Real-Time Collaboration** — Sync trip updates instantly across 3+ collaborators using Firebase.  
- 🔍 **Live Filtering & Sorting** — Dynamically search and sort reservations using Observer and Strategy patterns.  
- 🔒 **User Authentication** — Secure login and account management with Firebase Authentication.  

---

## 🧠 Tech Stack

| Layer | Technologies |
|-------|---------------|
| **Frontend** | Java, Android Studio, XML |
| **Backend** | Firebase Realtime Database & Authentication |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Design Patterns** | Observer, Strategy |

---

## 💻 My Contributions
- Developed **core modules** for trip reservation and logistics management.  
- Built **dynamic, modular UI flows** using Android XML and Java.  
- Implemented **Firebase backend integration** for real-time sync with <200ms latency during concurrent edits.  
- Applied **Observer and Strategy patterns** to support live filtering of 100+ entries.  
- Led end-to-end implementation of the **collaborative Logistics screen**, supporting dining and hotel reservation input with edit/view capabilities.  

---

## 🚀 How to Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/sritha8/WanderSync-App.git
cd WanderSync-App
```

### 2. Open in Android Studio
- Launch Android Studio
- Click "Open" and select the WanderSync-App project folder

### 3. Build the Project
- Wait for Gradle sync to complete
- Install any missing SDKs or dependencies if prompted

### 4. Set Up Firebase (optional but recommended)
- Create a Firebase project at firebase.google.com
- Download the google-services.json file
- Place it in the /app directory
- Ensure Firebase is configured for Authentication, Firestore, and Storage (if applicable)

### 5. Run the App
- Choose an emulator or connect a physical Android device
- Click the green ▶ Run button in Android Studio
