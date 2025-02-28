#  Документация Swagger

- JSON: `http://localhost:8080/v3/api-docs`

- YAML: `http://localhost:8080/v3/api-docs.yaml`

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

#  Инструкции для локального запуска 

##  1. Собираем проект  
Для того чтобы собрать проект, выполните команду:

```bash
mvn clean package
```

## 2. Собираем Docker-образ
Для сборки Docker-образа используйте команду:

```bash
docker compose build
```

## 3. Запуск контейнеров
Запустите контейнеры в фоновом режиме с помощью команды:

```bash
docker compose up -d
```

## 4. Взаимодействуем с контейнерами при необходимости

   - Остановить контейнеры:
     ```bash
      docker compose stop

   - Чтобы затем их снова запустить:
      ```bash
       docker compose start
      
   - Перезапустить контейнеры без пересборки:
     ```bash
       docker compose restart

## 5. Остановить контейнеры
Для остановки контейнеров и удаления их (без очистки содержимого базы данных):

```bash
docker compose down
```
