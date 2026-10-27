# 💬 Connect — Real-Time Chat Application

**A web-based real-time chat application built using Java, Spring Boot, WebSockets, and MySQL.**

🌐 **Live Demo:** Connect — Real-Time Chat

📂 **GitHub Repository:** https://github.com/Lasya2701/realtimechat

## 📌 About the Project

Connect is a real-time chat application designed to allow multiple users to communicate through a web interface. It uses WebSocket technology to support real-time communication between connected clients.

The application is developed using Java and Spring Boot, with MySQL for data persistence. It also provides an interface for sending messages and sharing media files.

## ✨ Features

- 💬 Real-time messaging using WebSockets
- 👥 Support for multiple users
- 🟢 Online member tracking
- 🖼️ Image sharing
- 🎥 Video and audio file sharing
- 🎙️ Voice recording interface
- 🗄️ MySQL database integration
- 🌐 Web-based interface accessible through a browser
- ☁️ Online deployment using Render

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot | Application framework |
| Spring WebSocket | Real-time communication |
| Spring Data JPA | Database interaction |
| MySQL | Data storage |
| HTML, CSS, JavaScript | Frontend |
| Maven | Dependency management and build |
| Docker | Application containerization |
| Render | Application hosting |
| Aiven | Cloud-hosted MySQL database |

## 🏗️ System Architecture

The application follows a client-server architecture.

1. **Frontend:** Provides the user interface for joining conversations and sending messages.
2. **Spring Boot Backend:** Handles application requests and WebSocket communication.
3. **WebSocket Handler:** Processes real-time messages between connected clients.
4. **MySQL Database:** Stores chat data through the application's persistence layer.
5. **Cloud Deployment:** Render hosts the application, while Aiven provides the cloud database.

## ⚙️ How It Works

1. A user opens the Connect website.
2. The user enters a display name and joins the conversation.
3. The application establishes a WebSocket connection.
4. Messages are exchanged through the backend in real time.
5. The backend interacts with MySQL for persistent data storage.
6. Users can use the available attachment controls to share supported media.

## 🚀 Run the Project Locally

### Prerequisites

Install the following software:

- Java Development Kit (JDK) 17
- Maven
- MySQL Server
- Git
- Eclipse IDE or another Java IDE

### 1. Clone the Repository

```bash
git clone https://github.com/Lasya2701/realtimechat.git
```

### 2. Open the Project

Import the project into Eclipse as an existing Maven project.

### 3. Configure the Database

Create a MySQL database and configure the following environment variables with your database connection details:

```text
DB_URL=jdbc:mysql://localhost:3306/realtime_chat
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

Replace the example username and password with your own local MySQL credentials. Ensure the database name matches your configuration.

**Security:** Never upload real database passwords, API keys, or other secrets to GitHub.

### 4. Run the Application

Run `RealtimechatApplication.java` from Eclipse as a Java application, or use Maven:

```bash
mvn spring-boot:run
```

### 5. Open the Website

Visit:

http://localhost:8080/

## ☁️ Deployment

The application is deployed on Render using Docker. The database is hosted on Aiven MySQL.

🌐 **Live Application:** https://realtimechat-z7kb.onrender.com/

## 🎯 Learning Outcomes

This project demonstrates practical experience with:

- Java backend development
- Spring Boot application development
- WebSocket-based communication
- Database integration using MySQL and JPA
- Maven dependency management
- Docker containerization
- Cloud deployment and environment configuration

## 🔮 Future Improvements

- 🔐 User authentication and secure login
- 👤 User profiles and profile pictures
- 🔒 Private and group chats
- 🔍 Message search functionality
- 📱 Improved mobile responsiveness
- ☁️ Reliable cloud storage for media uploads
- 🔔 Notifications for new messages

## 👩‍💻 Author

**Ch. Lasya**

B.Tech — Computer Science and Engineering

🔗 **GitHub:** https://github.com/Lasya2701

---

⭐ If you find this project interesting, consider giving the repository a star!
