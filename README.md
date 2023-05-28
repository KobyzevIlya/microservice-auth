# microservice-auth

## Запуск

Используется Postgresql
Запуск базы данных:
```
docker-compose up --build
```
из папки `docker`

Запуск приложения:
Точка входа - `Application.java`

## Endpoints
- Регистрация : `/api/auth/register`
- Авторизация : `/api/auth/login`
- Информация о пользователе : `/api/auth/user`

## Тестирование

Для тестирования необходимо открыть Postman, выбрать метод `POST`, запрос указать как `Body`->`raw`->`JSON`

Примеры запросов:

`/api/auth/register`:
```
{
  "username": "example_user",
  "email": "user@example.com",
  "password": "example_password",
  "role": "chef"
}
```

`/api/auth/login`:
```
{
  "email": "user@example.com",
  "password": "example_password",
}
```

Для `/api/auth/user` нужен метод `GET`:
```
/api/auth/user?token=example_token
```
