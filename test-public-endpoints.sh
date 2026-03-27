#!/bin/bash

# Тесты для проверки публичных эндпоинтов

BASE_URL="http://localhost:8080"

echo "🧪 Тестирование публичных эндпоинтов orta-back"
echo "================================================"
echo ""

# Проверка, запущен ли сервер
echo "1️⃣  Проверка доступности сервера..."
if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/swagger-ui.html" | grep -q "200\|302"; then
    echo "✅ Сервер доступен"
else
    echo "❌ Сервер не доступен. Запустите: ./mvnw spring-boot:run"
    exit 1
fi
echo ""

# Тест GET /api/events (без авторизации)
echo "2️⃣  Тест GET /api/events (без авторизации)..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/events")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ GET /api/events работает без авторизации"
    echo "Ответ: $BODY" | head -c 200
    echo "..."
else
    echo "❌ Ошибка: HTTP $HTTP_CODE"
    echo "Ответ: $BODY"
fi
echo ""

# Тест GET /api/events?type=SPORT (без авторизации)
echo "3️⃣  Тест GET /api/events?type=SPORT (без авторизации)..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/events?type=SPORT")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ GET /api/events?type=SPORT работает без авторизации"
else
    echo "❌ Ошибка: HTTP $HTTP_CODE"
fi
echo ""

# Тест OPTIONS (CORS preflight)
echo "4️⃣  Тест CORS preflight (OPTIONS)..."
CORS_HEADERS=$(curl -s -I -X OPTIONS \
    -H "Origin: http://localhost:5173" \
    -H "Access-Control-Request-Method: GET" \
    -H "Access-Control-Request-Headers: Content-Type" \
    "$BASE_URL/api/events")

if echo "$CORS_HEADERS" | grep -q "Access-Control-Allow-Origin"; then
    echo "✅ CORS preflight работает"
    echo "$CORS_HEADERS" | grep "Access-Control"
else
    echo "❌ CORS preflight не настроен"
fi
echo ""

# Тест POST /api/events (должен требовать авторизации)
echo "5️⃣  Тест POST /api/events (должен требовать авторизацию)..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -d '{"title":"Test","description":"Test","type":"SPORT","dateTime":"2026-12-31T12:00:00","location":"Test"}' \
    "$BASE_URL/api/events")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

if [ "$HTTP_CODE" = "401" ]; then
    echo "✅ POST /api/events требует авторизацию (401)"
else
    echo "⚠️  Неожиданный код: HTTP $HTTP_CODE (ожидался 401)"
fi
echo ""

echo "================================================"
echo "✅ Тестирование завершено!"
echo ""
echo "Чтобы протестировать с фронтенда:"
echo "1. Создайте React проект: npm create vite@latest"
echo "2. Установите axios: npm install axios"
echo "3. Сделайте запрос на http://localhost:8080/api/events"

