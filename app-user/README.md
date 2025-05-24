
# Speed Laundry - Android App Setup Guide

Aplikasi Speed Laundry terdiri dari dua aplikasi Android:
- **Admin App** (untuk mengelola laundry dan transaksi)
- **User App** (untuk pelanggan melakukan pemesanan laundry)

## 🛠️ Prasyarat

Pastikan sudah menginstall:
- Android Studio
- Java Development Kit (JDK)
- Emulator Android atau perangkat fisik

## Langkah Setup Android

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


<img src="https://github.com/user-attachments/assets/f965ad0d-e049-4642-9e23-0d6a0feeb1b0" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/36862621-ff9a-4ea9-9d32-b718a55bc8cd" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/0e1d3557-8cf6-4c3b-92bf-2ca5ed7a4a5f" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/28780c2f-6a39-4bb8-8b74-82b1aeba778d" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/52354d30-79cb-4955-ac86-cf50fb216644" style="width:150px; height:auto;" />
<img src="https://github.com/user-attachments/assets/a84d3c0a-9225-45ea-a896-bbc14b0750b1" style="width:150px; height:auto;" />


