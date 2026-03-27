# Интеграция фронтенда с orta-back API

## Изменения в бэкенде

### 1. CORS конфигурация
CORS настроен для работы с любыми источниками:
- Разрешены все origins (через patterns)
- Разрешены методы: GET, POST, PUT, PATCH, DELETE, OPTIONS
- Разрешены все заголовки
- `Access-Control-Allow-Credentials: true`

### 2. Публичные эндпоинты (без авторизации)
Следующие эндпоинты доступны без токена авторизации:

#### Auth
- `POST /auth/login` - Вход
- `POST /auth/register` - Регистрация
- `POST /auth/refresh` - Обновление токена

#### Events (Лента событий)
- `GET /api/events` - Получить все активные события
- `GET /api/events?type=SPORT` - Фильтр по типу события
- `GET /api/events/{id}` - Получить детали события

**Важно:** Для неавторизованных пользователей поле `isUserJoined` всегда будет `false`.

## Рекомендации для фронтенда

### Технологии
Рекомендую использовать:
1. **React** + **TypeScript** + **Vite** - современный и быстрый стек
2. **React Router** - для роутинга
3. **Axios** - для HTTP запросов
4. **TanStack Query (React Query)** - для управления серверным состоянием
5. **Zustand** или **Context API** - для глобального состояния (токены, пользователь)
6. **Tailwind CSS** или **Material-UI** - для стилизации

### Структура проекта

```
frontend/
├── src/
│   ├── api/              # API клиенты
│   │   ├── axios.ts      # Настройка axios
│   │   ├── auth.api.ts   # Auth endpoints
│   │   └── events.api.ts # Events endpoints
│   ├── components/       # Переиспользуемые компоненты
│   │   ├── EventCard.tsx
│   │   ├── EventList.tsx
│   │   └── Navbar.tsx
│   ├── pages/           # Страницы
│   │   ├── HomePage.tsx
│   │   ├── EventDetailPage.tsx
│   │   ├── LoginPage.tsx
│   │   └── ProfilePage.tsx
│   ├── hooks/           # Кастомные хуки
│   │   ├── useAuth.ts
│   │   └── useEvents.ts
│   ├── store/           # State management
│   │   └── authStore.ts
│   ├── types/           # TypeScript типы
│   │   └── api.types.ts
│   └── App.tsx
```

### Пример настройки Axios

```typescript
// src/api/axios.ts
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor для добавления токена
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor для обработки ошибок и refresh токена
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
          refreshToken,
        });
        
        const { accessToken, refreshToken: newRefreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', newRefreshToken);
        
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);
```

### Типы TypeScript

```typescript
// src/types/api.types.ts

export enum EventType {
  SPORT = 'SPORT',
  CULTURE = 'CULTURE',
  EDUCATION = 'EDUCATION',
  ENTERTAINMENT = 'ENTERTAINMENT',
  OTHER = 'OTHER',
}

export enum EventStatus {
  ACTIVE = 'ACTIVE',
  CANCELED = 'CANCELED',
  DONE = 'DONE',
}

export interface EventResponse {
  id: number;
  title: string;
  description: string;
  type: EventType;
  status: EventStatus;
  dateTime: string;
  location: string;
  maxParticipants: number | null;
  currentParticipants: number;
  isUserJoined: boolean;
  hostId: number;
  hostUsername: string;
  createdAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  phone?: string;
}

export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  username: string;
  email: string;
  role: string;
}
```

### API клиенты

```typescript
// src/api/auth.api.ts
import { apiClient } from './axios';
import { LoginRequest, RegisterRequest, JwtResponse } from '../types/api.types';

export const authApi = {
  login: async (data: LoginRequest): Promise<JwtResponse> => {
    const response = await apiClient.post<JwtResponse>('/auth/login', data);
    return response.data;
  },
  
  register: async (data: RegisterRequest): Promise<string> => {
    const response = await apiClient.post<string>('/auth/register', data);
    return response.data;
  },
  
  refreshToken: async (refreshToken: string): Promise<JwtResponse> => {
    const response = await apiClient.post<JwtResponse>('/auth/refresh', { refreshToken });
    return response.data;
  },
};

// src/api/events.api.ts
import { apiClient } from './axios';
import { EventResponse, EventType } from '../types/api.types';

export const eventsApi = {
  getAll: async (type?: EventType): Promise<EventResponse[]> => {
    const params = type ? { type } : {};
    const response = await apiClient.get<EventResponse[]>('/api/events', { params });
    return response.data;
  },
  
  getById: async (id: number): Promise<EventResponse> => {
    const response = await apiClient.get<EventResponse>(`/api/events/${id}`);
    return response.data;
  },
  
  join: async (id: number): Promise<EventResponse> => {
    const response = await apiClient.post<EventResponse>(`/api/events/${id}/join`);
    return response.data;
  },
  
  leave: async (id: number): Promise<EventResponse> => {
    const response = await apiClient.post<EventResponse>(`/api/events/${id}/leave`);
    return response.data;
  },
};
```

### Пример использования с React Query

```typescript
// src/hooks/useEvents.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { eventsApi } from '../api/events.api';
import { EventType } from '../types/api.types';

export const useEvents = (type?: EventType) => {
  return useQuery({
    queryKey: ['events', type],
    queryFn: () => eventsApi.getAll(type),
  });
};

export const useEvent = (id: number) => {
  return useQuery({
    queryKey: ['event', id],
    queryFn: () => eventsApi.getById(id),
    enabled: !!id,
  });
};

export const useJoinEvent = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (eventId: number) => eventsApi.join(eventId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['events'] });
    },
  });
};
```

### Пример компонента

```tsx
// src/pages/HomePage.tsx
import React from 'react';
import { useEvents } from '../hooks/useEvents';
import EventCard from '../components/EventCard';

const HomePage: React.FC = () => {
  const { data: events, isLoading, error } = useEvents();

  if (isLoading) return <div>Загрузка...</div>;
  if (error) return <div>Ошибка загрузки событий</div>;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Активные события</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {events?.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
    </div>
  );
};

export default HomePage;
```

## Особенности работы с API

### 1. Авторизация
- После успешного логина сохраняйте `accessToken` и `refreshToken` в localStorage
- Добавляйте `Authorization: Bearer <token>` ко всем защищенным запросам
- При получении 401 ошибки автоматически обновляйте токен через `/auth/refresh`

### 2. Лента событий
- Лента доступна без авторизации
- Для авторизованных пользователей `isUserJoined` показывает, присоединился ли пользователь к событию
- Для неавторизованных `isUserJoined` всегда `false`
- Показывайте кнопку "Присоединиться" только авторизованным пользователям

### 3. Обработка ошибок
API возвращает структурированные ошибки:
```json
{
  "timestamp": "2026-01-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Описание ошибки",
  "path": "/api/events/1"
}
```

### 4. Форматы дат
- Все даты приходят в формате ISO-8601: `2026-01-05T15:30:00`
- Используйте `date-fns` или `dayjs` для форматирования

## Запуск и тестирование

### Бэкенд
```bash
cd /home/mdi/IdeaProjects/orta-back
./mvnw spring-boot:run
```
API доступен на: `http://localhost:8080`

### Фронтенд (пример для Vite)
```bash
npm create vite@latest orta-front -- --template react-ts
cd orta-front
npm install
npm install axios @tanstack/react-query
npm run dev
```
По умолчанию dev-сервер запустится на: `http://localhost:5173`

## API Documentation
Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui.html`

