
# Speed Laundry - Android App Setup Guide

Aplikasi Speed Laundry terdiri dari dua aplikasi Android:
- **Admin App** (untuk mengelola laundry dan transaksi)
- **User App** (untuk pelanggan melakukan pemesanan laundry)

## 🛠️ Prasyarat

Pastikan sudah menginstall:
- Android Studio
- Java Development Kit (JDK)
- Emulator Android atau perangkat fisik

## Setup Android

1. **Buka project Android di Android Studio**
   - Masuk ke folder `admin_app/` untuk membuka aplikasi admin
   - Masuk ke folder `user_app/` untuk membuka aplikasi user

2. **Sync Gradle**
   - Android Studio akan otomatis mendeteksi dan mensinkronkan `build.gradle`

3. **Generate `google-services.json` dari Firebase**
   - Buka [Firebase Console](https://console.firebase.google.com/)
   - Buat project baru atau gunakan yang sudah ada
   - Tambahkan aplikasi Android dengan Package Name yang sesuai (`com.speedlaundry.admin` atau `com.speedlaundry.user`)
   - Download file `google-services.json`
   - Letakkan file `google-services.json` ke:
     - `admin_app/app/` untuk aplikasi admin
     - `user_app/app/` untuk aplikasi user

4. **Jalankan aplikasi**
   - Klik tombol Run ▶️ di Android Studio, pilih emulator atau device yang tersedia

---

✅ Aplikasi Android Speed Laundry siap dijalankan!

Pastikan backend Laravel berjalan agar fitur login, transaksi, dan notifikasi dapat berfungsi dengan baik.


<img src="https://github.com/user-attachments/assets/6f20313a-5d0a-4706-a183-7f5219217338" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/1b532fcb-365f-4fc8-95d1-a6c76e19834b" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/2c1f5bca-c45d-4665-b193-0c4e7d7d7d2c" style="width:150px; height:auto;" />

