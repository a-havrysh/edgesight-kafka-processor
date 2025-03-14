# EdgeSight Kafka Processor

Сервис для обработки данных обнаружения объектов в реальном времени, полученных из Kafka, и их сохранения в Elasticsearch с резервным копированием в PostgreSQL.

## Обзор проекта

EdgeSight Kafka Processor является частью системы **EdgeSight** - конвейера обработки данных распознавания объектов в реальном времени. Система предназначена для обработки высокочастотных событий обнаружения с устройств граничных вычислений, обеспечивая масштабируемость, отказоустойчивость и низкую задержку при извлечении данных.

![Architecture](documentation/Architecture.png)

## Ключевые особенности

* **Прием данных с высокой частотой** – Поддерживает до **100 обнаружений каждые 100мс** с нескольких граничных устройств
* **Масштабируемая потоковая архитектура** – Использует **Kafka** для разделенной обработки сообщений в реальном времени
* **Оптимизированное хранение** – Хранилище **Elasticsearch** для эффективного запроса и аналитики
* **Отказоустойчивость** – Резервное хранение в **PostgreSQL** при недоступности Elasticsearch
* **Автоматическая синхронизация** – Планировщик для восстановления данных из PostgreSQL в Elasticsearch

## Технологический стек

* **Java 17**
* **Spring Boot 3.1.5**
* **Spring Data JPA**
* **Spring Data Elasticsearch**
* **Spring Kafka**
* **PostgreSQL**
* **Elasticsearch**
* **Lombok**
* **MapStruct**

## Архитектура системы

EdgeSight Kafka Processor является одним из компонентов системы EdgeSight, которая включает:

1. **Edge компоненты** - системы, отправляющие данные об обнаружениях в бэкенд
2. **Data Ingestion Service** - сервис для получения данных с Edge компонентов и передачи в Kafka
3. **Kafka Processor Service** (данный проект) - сервис для обработки данных из Kafka и сохранения в Elasticsearch/PostgreSQL
4. **Data Retrieval Service** - сервис для получения данных из Elasticsearch и передачи клиентам

### Компоненты хранения данных

* **Kafka** - потоковая передача данных об обнаружениях
* **PostgreSQL** - резервное хранилище данных на случай проблем с Elasticsearch
* **Elasticsearch** - основное хранилище данных для гибкого запроса и аналитики

## Принцип работы

1. Сервис получает пакеты данных об обнаружениях из Kafka
2. Проверяет валидность данных
3. Сохраняет валидные данные в Elasticsearch
4. В случае недоступности Elasticsearch, сохраняет данные в PostgreSQL
5. Периодически запускает планировщик, который синхронизирует данные из PostgreSQL в Elasticsearch

## Модель данных

Основная сущность - `Detection` (Обнаружение), которая содержит следующие поля:
* `uniqueObjectId` - уникальный идентификатор объекта
* `objectType` - тип обнаруженного объекта
* `confidence` - уровень уверенности в обнаружении
* `timestamp` - время обнаружения
* `source` - источник обнаружения
* `location` - местоположение обнаружения

## Настройка и запуск

### Предварительные требования

* Java 17 или выше
* PostgreSQL
* Elasticsearch
* Kafka

### Конфигурация

Основные настройки находятся в файле `application.yml`:

```yaml
spring:
  application:
    name: edgesight-kafka-processor
  datasource:
    url: jdbc:postgresql://localhost:5432/edgesight
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false
  elasticsearch:
    uris: http://localhost:9200
    username: elastic
    password: changeme
kafka:
  bootstrap-servers: localhost:9092
  topic:
    detections: edgesight.detections
  consumer:
    group-id: edgesight-processor
    auto-offset-reset: earliest
    batch-size: 100
    concurrency: 3
    max-poll-records: 500
    max-poll-interval-ms: 300000
    session-timeout-ms: 30000
    heartbeat-interval-ms: 10000
scheduler:
  sync:
    enabled: true
    interval: 60000
```

### Запуск приложения

```bash
./gradlew bootRun
```

### Сборка проекта

```bash
./gradlew build
```

## Планы по развитию

1. Подключение Liquibase для управления схемой базы данных
2. Блокировка строк БД при синхронизации с elasticsearch 
3. Интеграция SchedLock для блокировки шедулеров между инстансами
4. Улучшение обработки сценариев, когда Elasticsearch и PostgreSQL недоступны одновременно
5. Добавление метрик для мониторинга работоспособности компонентов
6. Настройка алертов в каналы связи (Slack, Teams, Telegram) при проблемах с компонентами
7. Расширение тестового покрытия
8. Добавление генерации Docker-образа через Gradle plugin или Dockerfile