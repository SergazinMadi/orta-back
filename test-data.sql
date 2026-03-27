INSERT INTO Use (username, email, email_verified, phone_number, phone_verified, password, role, trust_score, created_at, updated_at, full_name)
VALUES
    ('alex_sport', 'alex@example.com', true, '+77001234567', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.8, NOW(), NOW(), 'Alexander Ivanov'),
    ('maria_culture', 'maria@example.com', true, '+77001234568', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.9, NOW(), NOW(), 'Maria Petrova'),
    ('dmitry_study', 'dmitry@example.com', true, '+77001234569', false, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.5, NOW(), NOW(), 'Dmitry Smirnov'),
    ('elena_yoga', 'elena@example.com', true, '+77001234570', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 5.0, NOW(), NOW(), 'Elena Sidorova'),
    ('artem_games', 'artem@example.com', true, '+77001234571', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.2, NOW(), NOW(), 'Artem Kozlov'),
    ('anna_music', 'anna@example.com', true, '+77001234572', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.7, NOW(), NOW(), 'Anna Morozova'),
    ('sergey_food', 'sergey@example.com', false, '+77001234573', false, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 3.9, NOW(), NOW(), 'Sergey Volkov'),
    ('olga_photo', 'olga@example.com', true, '+77001234574', true, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 4.6, NOW(), NOW(), 'Olga Novikova');

-- SPORTS EVENTS
INSERT INTO event (host_id, status, title, type, description, place_name, address, date_time, time_duration, max_participants, price_per_person, created_at)
VALUES
    (1, 'ACTIVE', '5v5 Football at Central Park', 'SPORT',
     'Looking for players for a friendly match. Amateur level. Bring sportswear, the ball is provided. After the game we can grab some tea together.',
     'Central Park', 'Abay Ave 1, football field',
     '2026-01-10 18:00:00', 90, 10, 0, NOW()),

    (1, 'ACTIVE', 'Morning Run at First President Park', 'SPORT',
     'Join a morning run. Distance around 5 km, comfortable pace. Suitable for beginners. Meeting at the main entrance.',
     'First President Park', 'Dostyk St, main entrance',
     '2026-01-08 07:00:00', 60, 15, 0, NOW()),

    (4, 'ACTIVE', 'Outdoor Yoga Session', 'SPORT',
     'Inviting everyone to an outdoor yoga session. Bring your own mat, comfortable clothes, and good mood. Suitable for all levels.',
     'Green Park', 'Satpayev St, recreation area',
     '2026-01-09 09:00:00', 75, 12, 500, NOW()),

    (5, 'ACTIVE', '3x3 Basketball Tournament', 'SPORT',
     'We are organizing a street basketball mini-tournament. Teams will be formed on site. Winners get symbolic prizes.',
     'Baluan Sholak Sports Complex', 'Raiymbek Ave 480',
     '2026-01-12 15:00:00', 120, 12, 1000, NOW()),

    (1, 'ACTIVE', 'City Cycling Ride', 'SPORT',
     'Route: Medeu -> Shymbulak -> back. About 25 km. Good physical condition required. Helmet is mandatory.',
     'Medeu Skating Rink', 'Medeu Gorge',
     '2026-01-15 10:00:00', 180, 8, 0, NOW()),

-- CULTURAL EVENTS
    (2, 'ACTIVE', 'Theater Visit: "Abai" Opera', 'CULTURE',
     'We are going to watch the classical opera "Abai". After the performance we can discuss it over coffee. Tickets are purchased individually.',
     'Kazakh State Academic Opera and Ballet Theater', 'Abylai Khan Ave 110',
     '2026-01-11 19:00:00', 180, 6, 0, NOW()),

    (6, 'ACTIVE', 'Jazz Evening at a Cafe', 'CULTURE',
     'Live jazz in a cozy cafe with great music and atmosphere. Minimum order in the cafe starts from 3000 KZT.',
     'Jazz Cafe', 'Baitursynov St 85',
     '2026-01-13 20:00:00', 120, 8, 0, NOW()),

    (2, 'ACTIVE', 'Almaty Museum Tour', 'CULTURE',
     'One-day tour of the city’s main museums: National Museum, Museum of Art, Kasteev Museum.',
     'Central Museum', 'Dostyk Ave 44',
     '2026-01-14 11:00:00', 240, 10, 2000, NOW()),

    (6, 'ACTIVE', 'Open-Air Movie Night', 'CULTURE',
     'Watching classic Kazakh cinema on a big screen in the park. Bring warm blankets and thermoses with hot tea.',
     '28 Panfilov Park', 'Gogol St 1',
     '2026-01-17 21:00:00', 150, 30, 0, NOW()),

    (2, 'ACTIVE', 'Dombra Masterclass', 'CULTURE',
     'Learn basic melodies on the dombra. Instruments are provided. Come explore Kazakh musical culture.',
     'Turan Cultural Center', 'Masanchi St 98',
     '2026-01-16 18:00:00', 90, 8, 1500, NOW()),

-- EDUCATIONAL EVENTS
    (3, 'ACTIVE', 'English Speaking Club', 'STUDY',
     'Practicing conversational English in an informal setting. Topics range from travel to technology. All levels welcome.',
     'Abai Library', 'Abay Ave 14',
     '2026-01-09 18:00:00', 90, 12, 0, NOW()),

    (3, 'ACTIVE', 'Workshop: Git and GitHub for Beginners', 'STUDY',
     'Learning Git basics: repositories, commits, branches, pull requests. Bring a laptop with Git installed.',
     'IT Hub', 'Rozybakiev St 247',
     '2026-01-11 15:00:00', 120, 15, 2000, NOW()),

    (3, 'ACTIVE', 'Book Club: Discussing "The Path of Abai"', 'STUDY',
     'Book club meeting. Discussing the novel "The Path of Abai" by Mukhtar Auezov.',
     'Books & Coffee', 'Zhibek Zholy St 135',
     '2026-01-10 19:00:00', 120, 10, 0, NOW()),

-- OTHER EVENTS
    (7, 'ACTIVE', 'Kazakh Cuisine Tasting', 'OTHER',
     'Trying traditional dishes in a cozy national restaurant. Beshbarmak, kuyrdak, baursaks, and tea with milk.',
     'Al-Aina Restaurant', 'Samal-2, Building 111',
     '2026-01-12 13:00:00', 120, 8, 5000, NOW()),

    (5, 'ACTIVE', 'Board Games: Munchkin', 'OTHER',
     'Playing popular board games. Rules will be explained for beginners.',
     'Tsiferblat Anti-Cafe', 'Pushkin St 71',
     '2026-01-13 17:00:00', 180, 6, 1000, NOW()),

