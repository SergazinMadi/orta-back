#!/bin/bash

# Скрипт для быстрого создания React фронтенда для orta-back

echo "🚀 Создание фронтенда для orta-back..."

# Создание проекта
npm create vite@latest orta-front -- --template react-ts

cd orta-front

# Установка зависимостей
echo "📦 Установка зависимостей..."
npm install
npm install axios @tanstack/react-query react-router-dom
npm install -D @types/node

echo "✅ Проект создан!"
echo ""
echo "Следующие шаги:"
echo "1. cd orta-front"
echo "2. Скопируйте файлы из примеров (см. FRONTEND_INTEGRATION.md)"
echo "3. npm run dev"
echo ""
echo "Фронтенд будет доступен на http://localhost:5173"
echo "Бэкенд должен быть запущен на http://localhost:8080"

