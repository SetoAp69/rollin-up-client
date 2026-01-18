# Rollin-up
![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/Language-Kotlin_2.0-7F52FF?logo=kotlin&logoColor=white) ![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?logo=jetpackcompose&logoColor=white) ![Arch](https://img.shields.io/badge/Architecture-Clean_%2B_MVVM-orange)
## Introduction

This is the client app for the **Rollin-up** thesis project. It is a native multiplatform application built using **Jetpack Compose** and **Compose Multiplatform** that interacts with a Kotlin Multiplatform (KMP) shared core.

The application serves as the primary interface for students to record attendance. It utilizes **Geofencing** and **Biometric/Device location** validation to ensure that attendance data is authentic and recorded only within the designated school boundaries.

The project provides a seamless experience for all school stakeholders through specialized client applications:

1.  **Mobile Client (Android):** A unified app for **Students** (to attend) and **Teachers** (to manage). _Note: iOS support is architecturally planned for future implementation._
    
3.  **Desktop Client (JVM):** A robust administrative dashboard for **Teachers** and **Admins**.

### Location-Based Attendance
* **Geofence Validation:** Automatically detects if the student is within the school radius (e.g., [School Name]) before allowing check-in.
* **Anti-Spoofing:** Implements checks to prevent mock location providers (Fake GPS).

##  Architecture

This project is built using **Layered Architecture** to ensure strict separation of concerns and data abstraction, using **MVVM (Model-View-ViewModel)** as design pattern approach while also applied 
**Clean Code Principle**. 

| Layer | Responsibility | Components |
| :--- | :--- | :--- |
| **UI (Presentation)** | Renders the screen and handles user input. | `Composables`, `Activities` |
| **Domain (Shared)** |  Contains Usecases  | `Usecases` |
| **Data (Shared)** |  Handles API calls and local storage. | `Ktor Client`, `DataStore` |

##  Tech Stack
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Navigation:** Jetpack Navigation Compose
* **Async:** Kotlin Coroutines & Flow
* **Image Loading:** Coil
* **Dependency Injection:** Koin 
* **Networking:** Ktor Client (Engine: Platform Specific Engine)
