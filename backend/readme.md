
# 🚀 Speed Laundry - Backend Installation Guide

Ini adalah panduan instalasi untuk backend API Speed Laundry yang dibangun menggunakan Laravel 8.

## ⚙️ Persyaratan Sistem

Pastikan Anda sudah menginstall:
- PHP >= 7.3
- Composer
- MySQL / MariaDB
- Git

## 🚀 Langkah Instalasi

1. **Clone repository (jika belum)**
   ```bash
   git clone https://github.com/13husen/speed-laundry.git
   cd speed-laundry/backend
   ```

2. **Install dependency Laravel**
   ```bash
   composer install
   ```

3. **Copy file environment**
   ```bash
   cp .env.example .env
   ```

4. **Edit file `.env` dan sesuaikan konfigurasi database & email**
   ```env
   APP_NAME=SpeedLaundry
   APP_ENV=local
   APP_KEY=

   DB_CONNECTION=mysql
   DB_HOST=127.0.0.1
   DB_PORT=3306
   DB_DATABASE=speed_laundry
   DB_USERNAME=root
   DB_PASSWORD=

   MAIL_MAILER=smtp
   MAIL_HOST=smtp.mailtrap.io
   MAIL_PORT=2525
   MAIL_USERNAME=your_username
   MAIL_PASSWORD=your_password
   MAIL_ENCRYPTION=tls
   MAIL_FROM_ADDRESS=no-reply@speedlaundry.com
   MAIL_FROM_NAME="Speed Laundry"
   ```

5. **Generate application key**
   ```bash
   php artisan key:generate
   ```

6. **Migrasi database**
   ```bash
   php artisan migrate
   ```

7. **(Opsional) Seed database**
   ```bash
   php artisan db:seed
   ```

8. **Jalankan aplikasi**
   ```bash
   php artisan serve
   ```

   Aplikasi akan berjalan di:  
   `http://127.0.0.1:8000`

## 🔐 Autentikasi

Backend ini menggunakan Laravel Sanctum. Untuk endpoint yang memerlukan autentikasi, gunakan token Bearer pada header:
```
Authorization: Bearer your_token
```

---

Backend siap digunakan! 🚀
