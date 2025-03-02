<img src='https://github.com/Developer-Harsh/Walkie-Talkie/blob/master/graphics/cover.png?raw=true'>

# Walkie-Talkie Application 🎙️📡  

Welcome, developers and GitHub visitors!  

I'm **Harsh Kumar Singh**, the creator of this **Walkie-Talkie** application, inspired by a series where soldiers communicated using traditional walkie-talkies. That concept sparked an idea—why not bring the same experience into a modern app? 🚀  

## 📌 About the App  
This application enables real-time communication using **WebRTC** technology, allowing users to:  
✅ **Send text messages** 💬  
✅ **Start video calls** 📹  
✅ **Initiate audio calls** 🎧  

WebRTC facilitates **SFU (Selective Forwarding Unit) streaming**, where a single host can stream, and multiple users can join as viewers. This ensures efficient video and audio transmission with minimal latency.

## ✨ Key Features  
- 🚀 **Splash Screen** for a smooth app launch  
- 🔑 **Onboarding Screen** to generate user credentials  
- 🏠 **Home Screen** displaying locally stored users  
- ➕ **Add Contact** functionality  
- ❌ **Delete Contact** feature  
- 🔔 **FCM Service** for push notifications  
- 💬 **Text Messaging** for instant communication  
- 📹 **Video Messaging** using WebRTC  
- 🎧 **Voice Messaging** for walkie-talkie-style interaction  
- 🔐 **End-to-End Encrypted Environment** for secure communication  
- 🌐 **WebRTC-based Communication** for real-time audio/video streaming  
- 🖥️ **System-Wide Overlay** to display received messages  
- 📋 **Copy ID** feature for easy sharing  
- 🎨 **Clean UI** with Jetpack Compose and a well-structured architecture

## 🚀 Future Considerations  
- 🛡 **Advanced Blocking Mechanism**: Implementing features like auto-reject, spam detection, and customizable block settings.  
- 📡 **Network Adaptation**: Enhancing WebRTC performance in low-bandwidth environments.  
- 🔄 **Message Sync & Backup**: Cloud-based message storage for device switching.  
- 🏷 **User Presence Indicator**: Show when a contact is online or offline.  
- 🆘 **Emergency Communication Mode**: Quick-access mode for critical situations.  

## 🛠️ How It Works  
1️⃣ **Launching the App:**  
   - Upon opening, the app presents an interface where users can swipe a button to generate credentials.  

2️⃣ **Adding Contacts:**  
   - Tap the **"Add Contact"** button at the bottom.  
   - Enter the unique **Walkie-Talkie ID** of the other user and save it.  
   - Assign a name to the contact (stored in a local Room database).  

3️⃣ **Communication Options:** Each contact appears on the home screen with three interaction buttons:  
   - 📩 **Text Message:** Opens a chat interface for typing and sending messages.  
   - 📹 **Video Message:** Initiates a WebRTC-based video call.  
   - 🎧 **Audio Message:** Starts a WebRTC-based audio conversation.  

4️⃣ **Message Delivery:**  
   - Messages and calls are transmitted securely using WebRTC, ensuring seamless real-time communication.  

## 🚀 Technologies & Tools Used  
### 🔹 **Frontend (Android App)**  
- **Language:** Kotlin  
- **Framework:** Jetpack Compose  
- **IDE:** Android Studio  

### 🔹 **Backend & Database**  
- **Backend:** Node.js  
- **Server:** Express.js  
- **Database:** MongoDB  
- **Messaging & Notifications:** Firebase Cloud Messaging (FCM)  

### 🔹 **Communication & Protocols**  
- **Real-time Communication:** WebRTC  
- **End-to-End Encryption:** Implemented for secure messaging  

### 🔹 **Development & Collaboration**  
- **Version Control:** Git  
- **Code Editor:** VS Code

## 🤝 Contributions Welcome!  
We **welcome contributions** to this repository! 🎉 If you're interested in improving the project, feel free to:  
- **Submit issues** for bugs or feature requests.  
- **Fork the repository**, make improvements, and submit a **pull request**.  
- **Suggest ideas** to enhance functionality.

## 🚀 Getting Started  
### 📌 Prerequisites  
Before setting up the project, ensure you have the following installed:  
- **Android Studio** (Latest version)  
- **Node.js** (v16 or later)  
- **MongoDB** (Cloud or Local instance)  
- **Firebase Console Account** (For FCM configuration)  
- **Git** (For version control)  

### 📥 Clone the Repository  
```sh
git clone https://github.com/your-repo-link/walkie-talkie.git
cd walkie-talkie
```

### 🔧 Setting Up the Backend
```sh
# Navigate to the server directory
cd server

# Install dependencies
npm install

# Configure environment variables (.env file)
echo "MONGO_URI=your-mongodb-connection-string" >> .env
echo "PORT=YOUR_PORT" >> .env

# Start the backend server
node server.js
```

### Run App in Device
```sh
# Be connected to Internet

Open project in Android Studio IDE

# Wait for build Finish
# After build => Run the app on an emulator or physical device:

Click on the ▶️ Run button or 🐞 Debug symbol in Android Studio.
Ensure the device is connected to the internet for real-time communication.
```

## 📜 License  

This project is available for **learning and educational purposes only**. You are free to explore, modify, and use the code for personal or academic learning.  

🚫 **Restrictions:**  
- Do **NOT** use this project for **commercial purposes**.  
- Do **NOT** publish or redistribute this project as your own.  
- Do **NOT** modify and rebrand it under your name.  

Respect the efforts of the developers and contribute responsibly. 🙌  

#### This project is an exciting blend of **classic walkie-talkie functionality** and **modern WebRTC technology**, making communication fast, interactive, and fun! 🎉 
