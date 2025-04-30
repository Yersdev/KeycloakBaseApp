
---

# 🔐 KeycloakBase

**KeycloakBase** — это базовый шаблон Java-приложения с интеграцией **Keycloak** для аутентификации и авторизации. Он помогает быстро развернуть защищённые микросервисы с поддержкой **OAuth2 / OpenID Connect** на базе **Spring Boot**.

---

## 📁 Структура проекта

```
KeycloakBase/
├── .mvn/              # Maven Wrapper
├── src/
│   └── main/
│       ├── java/      # Java-классы приложения
│       └── resources/ # application.yml и другие конфигурации
├── pom.xml            # Описание зависимостей и сборки
├── mvnw / mvnw.cmd    # Скрипты запуска Maven Wrapper (Unix / Windows)
└── .gitignore         # Исключения для Git
```

---

## 🚀 Быстрый старт

1. **Клонируйте репозиторий:**

```bash
git clone https://github.com/Yersdev/KeycloakBase.git
cd KeycloakBase
```

2. **Соберите проект:**

```bash
./mvnw clean install
```

3. **Запустите приложение:**

```bash
./mvnw spring-boot:run
```

---

## 📬 Примеры API-запросов (Postman)

Коллекция содержит основные запросы для работы с Keycloak через ваше приложение:

| 🔧 Запрос              | Метод | URL                                      | Описание                                    |
|------------------------|--------|-------------------------------------------|---------------------------------------------|
| 🔐 Register User       | POST   | `http://localhost:8081/auth/register`     | Регистрация нового пользователя             |
| 🔑 Login User          | POST   | `http://localhost:8081/auth/login`        | Получение access и refresh токенов         |
| ♻️ Refresh Token       | POST   | `http://localhost:8081/auth/refresh`      | Обновление access токена по refresh токену |
| 🚪 Logout User         | POST   | `http://localhost:8081/auth/logout`       | Выход из системы (отзыв refresh токена)    |
| 👤 Get Current User    | GET    | `http://localhost:8081/users/me`          | Получение информации о текущем пользователе |
| ✏️ Update User         | PUT    | `http://localhost:8081/users/update`      | Обновление информации о пользователе        |

### 📥 Импорт в Postman:

1. Откройте Postman → **File → Import**
2. Загрузите файл: [`Keycloak Auth API Copy.postman_collection.json`](./Keycloak%20Auth%20API%20Copy.postman_collection.json)

### 🔐 Переменные:

- `{{access_token}}` — используется для авторизованных запросов.
- `{{refresh_token}}` — используется для обновления или отзыва токена.

---

## 🔧 Настройка Keycloak

1. Запустите локальный сервер Keycloak (Docker или standalone).
2. Импортируйте JSON-файлы конфигурации **Realm** и **Client** из директории `keycloak-config/`.
3. Убедитесь, что настройки в `application.yml` соответствуют вашему окружению.

Пример конфигурации:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/your-realm
      client:
        registration:
          keycloak:
            client-id: springsecurity
            client-secret: your-client-secret
            scope: openid
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/your-realm
            user-name-attribute: preferred_username
```

---

## 📄 Документация API

- **Swagger UI** (после запуска приложения):  
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

- **Postman коллекция:**  
  [`Keycloak Auth API Copy.postman_collection.json`](./Keycloak%20Auth%20API%20Copy.postman_collection.json)

---

## 🧪 Тестирование

Вы можете использовать **Postman** или **Swagger UI** для отправки запросов и проверки поведения API.

Перед тестированием убедитесь, что:

- Сервер Keycloak запущен.
- Импортированы `Realm` и `Client` настройки.
- Вы получили **JWT-токен** после логина.

---

## ✅ Требования

| Компонент         | Версия        |
|-------------------|---------------|
| Java              | 11 или выше   |
| Maven             | 3.6+          |
| Keycloak Server   | 20+           |

---

## 📌 Примечания

- Проект находится в активной разработке.
- Сообщайте об ошибках или предложениях через [Issues](https://github.com/Yersdev/KeycloakBase/issues).
- JSON-конфигурации находятся в директории `keycloak-config/`.

---

## 📦 Планы (TODO)

- [ ] Примеры защищённых REST-эндпоинтов
- [ ] Role-based доступ и разграничение прав
- [ ] Интеграция Docker Compose для Keycloak + приложения
- [ ] CI/CD пайплайн (GitHub Actions)

---
