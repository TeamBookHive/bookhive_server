# Bookchive Server

> 빠르게 기록하고 쉽게 분류하는 요즘 독서 아카이빙, 북카이브 ✨

![bookchive-preview](https://github.com/user-attachments/assets/418e0171-b92d-4529-b3ff-6f4b32e5faf2)

<br>

## 📓 Main Feature

### 빠른 기록
- OCR 텍스트 인식 및 문장 단위 구분
- LLM을 활용한 인식 텍스트 오탈자 처리

### 간편한 분류
- **Clova Studio**를 활용한 AI 태그 추천
- 기존 저장 태그 중 유사 태그 추천
- 아카이브에 어울리는 새로운 태그 생성

### 필요한 구절 검색
- **Clova Studio**를 활용한 AI 아카이브 검색
- LLM 질문 분석을 통해 아카이브 목록 검색

<br>

## 📂 Project Structure

```markdown
server
├── domain
│   ├── user
│   ├── post
│   └── tag
│       ├── dto
│       ├── entity
│       ├── controller
│       ├── service
│       └── repository
├── global
│   ├── config
│   ├── inteceptor
│   └── exception
└── ServerApplication
```

<br>

## ☁️ Server Architecture
![bookchive-ncp-architecture](https://github.com/user-attachments/assets/7d1befa5-007e-4db7-a85e-23f1013f2c7b)

<!--
<br>

## 📃 Database Schema

<br>

## 💻 Getting Started
-->

<br>

## 🔨 Tech Stack

<p align="center">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=junit5&logoColor=white"/>
  <img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/>
  <img src="https://img.shields.io/badge/Naver Cloud Platform-03C75A?style=flat-square&logo=naver&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=white"/>
</p>
