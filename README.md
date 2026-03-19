# 📱 My Profile App — MVVM Edition

<p align="center">
  <img src="screenshots/screenshot_1.png" width="180" style="border-radius:12px"/>
  &nbsp;&nbsp;
  <img src="screenshots/screenshot_2.png" width="180" style="border-radius:12px"/>
  &nbsp;&nbsp;
  <img src="screenshots/screenshot_3.png" width="180" style="border-radius:12px"/>
</p>
<p align="center">
  <img src="screenshots/screenshot_4.png" width="180" style="border-radius:12px"/>
  &nbsp;&nbsp;
  <img src="screenshots/screenshot_5.png" width="180" style="border-radius:12px"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Compose_Multiplatform-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/MVVM-FF6B35?style=for-the-badge&logo=databricks&logoColor=white"/>
</p>

---

## 👨‍💻 Identitas Mahasiswa

| Nama | NIM | Prodi |
|------|-----|-------|
| Muhammad Fadhilah Akbar | 123140003 | Teknik Informatika |

📌 **Tugas 4 — IF25-22017 Pengembangan Aplikasi Mobile**
Institut Teknologi Sumatera · Genap 2025/2026

---

## 📖 Tentang Aplikasi

Pengembangan lanjutan dari Profile App Tugas 3 dengan menerapkan **State Management** dan **arsitektur MVVM** (Model-View-ViewModel). UI kini bersifat reaktif — setiap perubahan state secara otomatis memperbarui tampilan tanpa perlu refresh manual.

---

## ✨ Fitur Utama

| Fitur | Keterangan |
|-------|------------|
| 🖼️ Hero Header | Foto profil circular dengan gradient & status badge (dipertahankan dari Tugas 3) |
| 📊 Stats Card | Menampilkan Angkatan, Prodi, dan Jumlah Proyek |
| 📝 Bio Card | Deskripsi singkat tentang diri |
| 📞 Info Kontak | Email, telepon, lokasi, institusi dengan `AnimatedVisibility` |
| ✏️ Edit Profil | Form edit nama, titel, bio, email, telepon, dan lokasi |
| ✅ Validasi Input | Error message jika nama dikosongkan saat menyimpan |
| 🌙 Dark Mode | Toggle light/dark mode di TopAppBar, state tersimpan di ViewModel |

---

## 🏗️ Arsitektur MVVM

```
App.kt
│
├── Data Layer
│   └── UserProfile          → data class profil pengguna
│
├── ViewModel Layer
│   ├── ProfileUiState       → data class seluruh state UI
│   └── ProfileViewModel     → mengelola state & business logic
│
└── View Layer
    ├── App()                → entry point, inject ViewModel
    ├── ViewProfileScreen()  → tampilan profil utama
    ├── EditProfileScreen()  → form edit profil
    └── Composable functions → HeroHeader, BioCard, ContactCard, dll
```

**Alur data:**
```
UserProfile → ProfileUiState → ProfileViewModel → ViewProfileScreen
                                    ↑
              EditProfileScreen → onSave / onCancel
```

---

## 🧩 Composable Functions

```
App()                →  Entry point, AnimatedContent dark mode
ViewProfileScreen()  →  Screen utama tampilan profil
EditProfileScreen()  →  Screen form edit profil
HeroHeader()         →  Header gradient + foto profil (dari Tugas 3)
BioCard()            →  Kartu bio/deskripsi diri
ContactCard()        →  Kartu informasi kontak
StatCard()           →  Kartu statistik individual  
InfoItem()           →  Baris item kontak (emoji + label + value)
LabeledTextField()   →  Stateless TextField dengan state hoisting
DarkModeSwitch()     →  Stateless toggle dark/light mode
```

---

## 📐 Konsep yang Diterapkan

### State Management
```kotlin
// remember + mutableStateOf untuk state yang survive recomposition
var uiState by mutableStateOf(ProfileUiState())
    private set
```

### State Hoisting — LabeledTextField
```kotlin
// Stateless: value turun dari ViewModel, event naik ke ViewModel
@Composable
fun LabeledTextField(
    value         : String,              // ← state turun ↓
    onValueChange : (String) -> Unit,   // ← event naik ↑
    ...
)
```

### Immutable Update dengan .copy()
```kotlin
// Setiap update state menggunakan .copy() — tidak mutate langsung
uiState = uiState.copy(
    profile = uiState.profile.copy(name = editName.trim())
)
```

---

## 🛠️ Yang Digunakan

* **State** — `mutableStateOf` `remember` `ProfileUiState`
* **ViewModel** — `ProfileViewModel` dengan state hoisting callbacks
* **Layout** — `Column` `Row` `Box` `Scaffold` `TopAppBar`
* **Komponen** — `Text` `Button` `Card` `Switch` `OutlinedTextField`
* **Modifier** — `padding` `clip` `background` `border` `fillMaxWidth` `scale`
* **Animasi** — `AnimatedVisibility` `AnimatedContent` `expandVertically` `fadeIn` `fadeOut`

---

## 📸 Deskripsi Screenshot

| Screenshot | Keterangan |
|------------|------------|
| `screenshot_1.png` | Tampilan profil utama — light mode |
| `screenshot_2.png` | Tampilan profil utama — dark mode |
| `screenshot_3.png` | Kontak ditampilkan (`AnimatedVisibility`) |
| `screenshot_4.png` | Form edit profil |
| `screenshot_5.png` | Validasi error saat nama dikosongkan |

---

## ▶️ Cara Menjalankan

```bash
git clone https://github.com/123140003-MuhammadFadhilahAkbar/123140003_Tugas4PAM.git
```

Buka di Android Studio → Sync Gradle → Run ▶️

---

<p align="center">
  Made with ❤️ by Muhammad Fadhilah Akbar · ITERA 2025
</p>
