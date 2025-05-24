
# 🚀 Speed Laundry - Backend Installation Guide

Ini adalah panduan instalasi untuk backend API Speed Laundry yang dibangun menggunakan Laravel 8.

## ⚙️ Persyaratan Sistem

Pastikan Anda sudah menginstall:
- PHP >= 7.3
- Composer
- MySQL / MariaDB
- Git

## 🚀 Langkah Instalasi

1. **Install dependency Laravel**
   ```bash
   composer install
   ```

2. **Copy file environment**
   ```bash
   cp .env.example .env
   ```

3. **Edit file `.env` dan sesuaikan konfigurasi database & email**
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
   MAIL_HOST=smtp_server_host
   MAIL_PORT=2525
   MAIL_USERNAME=your_username
   MAIL_PASSWORD=your_password
   MAIL_ENCRYPTION=tls
   MAIL_FROM_ADDRESS=no-reply@speedlaundry.com
   MAIL_FROM_NAME="Speed Laundry"
   ```

4. **Generate application key**
   ```bash
   php artisan key:generate
   ```

5. **Migrasi database**
   ```bash
   php artisan migrate
   ```

6. **(Opsional) Seed database**
   ```bash
   php artisan db:seed
   ```

7. **Jalankan aplikasi**
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


## 🔔 Konfigurasi Firebase Cloud Messaging (FCM)

Aplikasi ini menggunakan **Firebase Cloud Messaging (FCM)** untuk mengirim notifikasi push. Ikuti langkah-langkah berikut:

### 1. Buat Project di Firebase Console

- Buka [Firebase Console](https://console.firebase.google.com/)
- Klik **"Add project"** dan ikuti instruksinya.

### 2. Ambil Service Account Key

- Masuk ke Firebase Console > **Project settings** (ikon gear) > **Service accounts**
- Klik **"Generate new private key"** → file `.json` akan terunduh

### 3. Letakkan File JSON ke Laravel

Simpan file tersebut ke:

```
storage/app/firebase/firebase_credentials.json
```

> ⚠️ Jangan commit file ini ke GitHub! anda bisa letakan di .gitignore

Tambahkan path di file `.env`:

```
FCM_CREDENTIAL_PATH=storage/app/firebase/firebase_credentials.json
FCM_CREDENTIAL_PATH_ADMIN=storage/app/firebase/firebase_credentials_admin.json
```

### 4. Konfigurasi di Laravel

di `config/laundry.php`:

```php
return [
    'fcm_key' => [
        'fcm_service_account_path' => storage_path('app/' . env('FCM_CREDENTIAL_PATH')),
        'fcm_service_account_path_admin' => storage_path('app/' . env('FCM_CREDENTIAL_PATH_ADMIN')),
    ],
];
```

Backend siap digunakan! 🚀
