# 🧠 MindMate — Mental Wellness Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2%2B-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue)
![Gemini AI](https://img.shields.io/badge/Gemini%20AI-API-FFD700)
![License](https://img.shields.io/badge/License-MIT-orange)

**The AI-powered backend for MindMate**, a mental wellness app featuring mood tracking, journaling, gamification, and community support with intelligent task generation.

**Frontend :** https://github.com/rath23/mindmate-app

---

## 🌟 Features

### Core Modules
| Feature                | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| **Mood Analytics**     | Tracks daily moods with streaks, history, and AI-powered self-care tips     |
| **Journaling System**  | Secure journal entries with XP rewards and reflection prompts               |
| **Badge Gamification** | 15+ unlockable badges for user achievements                                 |
| **AI Daily Tasks**     | Gemini-generated personalized tasks (regenerated every 24h via scheduler)   |
| **Moderated Group Chat** | Real-time chat with 3-strike warning system and automated bans              |
| **XP & Streaks**       | Dynamic XP calculation (moods + journals + streaks)                         |

---

## 🛠 Tech Stack

**Backend**
- **Spring Boot 3.2** (Java 17)
- **PostgreSQL** (User data, moods, journals)
- **Gemini API** (Task generation & self-care recommendations)
- **Firebase Cloud Messaging** (Push notifications)
- **Spring Security** (JWT authentication)

**Key Libraries**
- Lombok (Boilerplate reduction)
- Spring Data JPA (Database operations)
- Spring Scheduler (Automated tasks)
- Gson (JSON processing)

---

## 🚀 Installation

### Prerequisites
- Java 17+
- PostgreSQL 16+
- Gemini API key
- Firebase service account JSON

### Steps
1. Clone repo:
   ```bash
   git clone https://github.com/rath23/mindmate.git
