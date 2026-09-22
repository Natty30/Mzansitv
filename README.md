# Mzansi TV 🇿🇦

A native Android live television streaming application built for South Africa's public broadcast television network, crafted with Kotlin, Jetpack Compose, and Media3 ExoPlayer.

Developed by **[Natty30](https://github.com/Natty30)**.

---

## 📺 Overview

**Mzansi TV** delivers live, low-latency HLS (`.m3u8`) streaming for all key South African Broadcasting Corporation (SABC) channels:

- **SABC News**: 24-hour national & international breaking news and current affairs.
- **SABC 1**: Premier youth & general entertainment, local dramas, and cultural programming.
- **SABC 2**: Family entertainment, lifestyle, multilingual dramas, and education.
- **SABC 3**: International cinema, contemporary lifestyle series, and business programs.
- **SABC Sport**: High-energy live sporting events, PSL coverage, and athletics.
- **SABC Education**: Comprehensive STEM, curriculum tutoring, and student broadcasts.

---

## ✨ Features

- **HLS Live Streaming Engine**: Powered by **AndroidX Media3 ExoPlayer** with automatic stream reconnection, aspect ratio toggling (Fit/Zoom), mute controls, and fullscreen view.
- **Resource-Optimized Rendering**: Utilizes hardware-accelerated `TextureView` rendering with automatic software decoder fallback (`setEnableDecoderFallback(true)`), preventing hardware overlay resource exhaustion.
- **Local Persistence with Room**: Star and save your favorite channels offline with instant Room database synchronization.
- **Fast Filter & Search**: Instantly browse by category (*News, Entertainment, Sports, Education*) or search channels in real time.
- **Modern Material Design 3**: Obsidian cinematic theme accented with South African gold, emerald, and red.

---

## 🛠️ Tech Stack

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose (Material 3)
- **Streaming**: AndroidX Media3 ExoPlayer (`androidx.media3.exoplayer`, `androidx.media3.exoplayer.hls`, `androidx.media3.ui`)
- **Image Loading**: Coil Compose
- **Database**: AndroidX Room (SQLite local persistence)
- **Architecture**: MVVM with Kotlin Coroutines & StateFlow
- **Minimum SDK**: 24 (Android 7.0+) | **Target SDK**: 35

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (or newer)
- JDK 17+
- Android SDK 35

### Build and Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Natty30/Mzansitv.git
   cd Mzansitv
   ```
2. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
3. Run tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 🚀 Production & Release Configuration

### 💰 Google AdMob Integration
The app integrates the official Google Mobile Ads SDK (`play-services-ads:23.6.0`) with automated development test ad isolation and production ad routing:

- **Debug Builds**: Automatically use official Google AdMob sample test IDs (`ca-app-pub-3940256099942544...`) to prevent test click policy violations.
- **Release Builds**: Automatically inject production AdMob units:
  - **AdMob App ID**: `ca-app-pub-1626170613708164~3884704370`
  - **Banner Ad Unit ID**: `ca-app-pub-1626170613708164/4411407272`
  - **Interstitial Ad Unit ID**: `ca-app-pub-1626170613708164/8059753109`
- **Ad Placements**:
  - **Banner**: Cleanly anchored in the bottom bar during channel browsing. Never obstructs playback controls or full-screen viewing.
  - **Interstitial**: Preloaded in background with frequency capping (at most once every 3 minutes) triggered exclusively on channel transitions.
- **User Consent (Google UMP SDK)**: Integrated Google User Messaging Platform for GDPR and South African POPIA compliance, with an in-app "Consent Settings" dialog.

---

## 🔒 Privacy Policy & Data Safety (Google Play Compliance)

- **Dedicated In-App Screen**: Accessible via the Privacy shield button on the top header.
- **Online Privacy Policy URL**: [https://github.com/Natty30/Mzansitv/blob/main/PRIVACY_POLICY.md](https://github.com/Natty30/Mzansitv/blob/main/PRIVACY_POLICY.md) (and `public/privacy-policy.html`).
- **Google Play Data Safety Declaration**:
  - **Does the app collect or share user data?** Yes (via Google AdMob third-party SDK).
  - **Data Collected**:
    - Device or other IDs (Advertising ID / Android ID) for Advertising and Fraud Prevention.
    - Approximate location (coarse IP-level) for Ad delivery.
    - App performance and diagnostics (crash telemetry for ad rendering).
  - **Data encrypted in transit?** Yes (all network traffic is encrypted via HTTPS/TLS 1.3).
  - **User accounts?** No user account creation or login required.
  - **Permissions declared in Manifest**: Only `INTERNET` and `ACCESS_NETWORK_STATE`.

---

## 📦 Production Artifacts

- **Signed Release Android App Bundle (AAB)**: `app/build/outputs/bundle/release/app-release.aab` *(Ready for Google Play Console upload)*
- **Signed Release APK**: `app/build/outputs/apk/release/app-release.apk`
- **Upload Keystore**: `./my-upload-key.jks` (Alias: `upload`)

---

## 👤 Author

**Natty30**
- GitHub: [@Natty30](https://github.com/Natty30)
- Repository: [Mzansi TV](https://github.com/Natty30/Mzansitv)
