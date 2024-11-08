
# Real-Time Chatting Application

This is a **Real-Time Chatting Application** built for Android, using Java, Firebase, and native Android components, designed as a clone of WhatsApp. It supports individual messaging, notifications, and real-time updates, leveraging Firebase as the backend.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Firebase Setup](#firebase-setup)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Features
- **User Authentication** (Email and Password)
- **Real-Time Chatting**: Send and receive messages instantly
- **User Status**: Online/offline status visibility
- **Notifications**: Push notifications for new messages
- **Profile Management**: Update display name, profile picture
- **Search**: Search users by name
- **Firebase Database & Storage Integration**

## Prerequisites
- Android Studio (latest version recommended)
- Java Development Kit (JDK 8 or above)
- Firebase account
- Git for version control

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/KunduVineet/Real-Time-Chatting-Application.git
   cd Real-Time-Chatting-Application
   ```

2. Open the project in **Android Studio**.

3. Sync the project with Gradle files.

4. Follow the [Firebase Setup](#firebase-setup) steps to integrate Firebase.

## Project Structure

The project is structured as follows:
```
📂 Real-Time-Chatting-Application
├── 📂 app
│   ├── 📂 src
│   │   ├── 📂 main
│   │   │   ├── 📂 java/com/example/chatapp
│   │   │   │   ├── MainActivity.java          # Entry point of the application
│   │   │   │   ├── LoginActivity.java         # User login screen
│   │   │   │   ├── RegisterActivity.java      # User registration screen
│   │   │   │   ├── ChatActivity.java          # Chat screen for messaging
│   │   │   │   ├── ProfileActivity.java       # User profile management
│   │   │   │   ├── adapters                   # Adapters for RecyclerView
│   │   │   │   ├── models                     # Model classes for users, messages
│   │   │   │   └── utils                      # Utility classes for Firebase interaction, notifications
│   │   │   └── 📂 res                         # XML resources (layouts, drawables, values)
│   │   │       ├── 📂 layout                  # Layout XML files
│   │   │       ├── 📂 drawable                # Drawable assets
│   │   │       └── 📂 values                  # Values (strings, colors, styles)
└── 📄 build.gradle                            # Gradle build configuration
```

## Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/) and create a new project.
2. Add an Android app to your Firebase project, entering your package name.
3. Download the `google-services.json` file and place it in the `app` directory of the project.
4. Enable **Authentication** with Email/Password in Firebase Console.
5. Enable **Realtime Database** and **Cloud Storage** in Firebase Console.
6. Sync Firebase with your Android project by adding dependencies to your `build.gradle` file.

## Usage

1. Launch the app on your Android emulator or a real device.
2. Register a new user or log in if you already have an account.
3. Start chatting with other users in real-time!

## Technologies Used
- **Java**: Programming language for Android development.
- **Firebase Authentication**: User authentication.
- **Firebase Realtime Database**: Real-time data storage for messages and users.
- **Firebase Storage**: Storage for profile pictures.
- **AndroidX Libraries**: Modernized Android components.
- **XML**: Layouts for UI design.

## Contributing
Contributions are welcome! Feel free to submit a Pull Request.

## License
This project is licensed under the MIT License.

