# 🚀 Speed Laundry

**Speed Laundry** adalah solusi digital modern untuk memudahkan operasional bisnis laundry. Aplikasi ini dirancang untuk memfasilitasi transaksi laundry yang efisien dan terorganisir, dengan sistem yang mendukung driver, admin, dan pelanggan dalam satu ekosistem.

---

## 📱 Fitur Utama

- 🧺 **Sistem Jemput-Antar**  
  Driver melakukan penjemputan, pengecekan, dan pengantaran cucian, dengan update status secara real-time melalui push notification.

- 📦 **Detail Transaksi Lengkap**  
  Pelanggan dan admin dapat melihat rincian transaksi secara transparan dan detail.

- 💳 **Pembayaran via Transfer Manual**  
  Bukti transfer dikirimkan melalui WhatsApp admin. *(Akan diintegrasikan dengan Payment Gateway ke depannya.)*

- 🧑‍💼 **Manajemen Admin**  
  Admin dapat mengelola kategori, tipe laundry, jenis pakaian, serta mengekspor laporan transaksi – semua dalam satu aplikasi.

---

## 🧱 Arsitektur & Teknologi

### 🎨 Frontend
- Android Studio
- Java
- XML Layout

### 🔧 Backend & Database
- Laravel 8
- MySQL

### 📦 Library & Integrasi
- Firebase Cloud Messaging (FCM) – Push Notification
- SMTP Mail – Untuk login, registrasi, dan verifikasi
- Laravel Sanctum – OAuth2 Authentication

---

## 📂 Struktur Aplikasi

- `admin-app/` → Aplikasi Android untuk admin
- `admin-user/` → Aplikasi Android untuk pengguna
- `backend/` → API backend Laravel

---

## 🔮 Future Enhancements

- 💰 **Integrasi Midtrans**: Pembayaran via QRIS & Virtual Account Bank
- 📍 **Pelacakan Lokasi Driver**: Tracking real-time saat penjemputan & pengantaran
- 🧾 **Pengiriman Invoice Otomatis**: Invoice akan dikirim via email setelah pembayaran

---

## 🚀 Status Proyek

- ✅ Versi awal sudah dapat digunakan
- 🛠 Beberapa fitur lanjutan dalam tahap pengembangan

---

## 🙌 Kontribusi

Pull request dan saran sangat terbuka! Jika ingin membantu pengembangan atau perbaikan fitur, silakan fork dan ajukan PR.

---

## 📄 Lisensi

[MIT License](LICENSE)

