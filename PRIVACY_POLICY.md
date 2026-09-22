# Privacy Policy for Mzansi TV

**Effective Date:** September 22, 2026  
**Last Updated:** September 22, 2026  
**Application Name:** Mzansi TV  
**Package Name:** `com.aistudio.mzansitv.live`  
**Developer:** Natty30  
**Contact Email:** zingelwayocivilandplant@gmail.com  

---

## 1. Overview
Mzansi TV ("the App") is an Android application designed to provide live streaming access to South African public broadcast television channels (SABC 1, SABC 2, SABC 3, SABC News, SABC Sport, and SABC Education). 

This Privacy Policy explains how information is collected, used, and shared when you use the App, in compliance with Google Play Developer Program Policies, the South African Protection of Personal Information Act (POPIA), and the European Union General Data Protection Regulation (GDPR).

---

## 2. Information We Collect and Process

### A. Information Stored Locally on Your Device
- **Favorite Channels:** When you star/favorite channels, this preference is stored locally on your device in an on-device SQLite database (AndroidX Room). This data is never sent to external servers and remains strictly on your device.

### B. Information Automatically Collected by Third-Party Services
The App uses **Google AdMob** to serve advertisements. Google AdMob may collect and process certain device and diagnostic data:
- **Device Identifiers:** Google Advertising ID (GAID / AAID), Android ID.
- **Network and Location:** IP address, internet service provider, coarse geographic location (city/country level inferred from IP).
- **Diagnostics and Performance:** App launch telemetry, ad impressions, ad clicks, crash diagnostics, and anti-fraud interaction data.

For more information on how Google processes ad data, please refer to Google's Privacy & Terms:  
👉 [https://policies.google.com/privacy](https://policies.google.com/privacy) and [https://policies.google.com/technologies/ads](https://policies.google.com/technologies/ads)

---

## 3. Permissions Used by the Application

The App requests only standard, least-privilege install-time permissions:
- `android.permission.INTERNET`: Strictly necessary to retrieve HTTP Live Streaming (HLS `.m3u8`) video playlists, video segments, and display advertisements.
- `android.permission.ACCESS_NETWORK_STATE`: Strictly necessary to monitor network connectivity status (Wi-Fi vs Mobile Data) to provide uninterrupted video buffering.

Mzansi TV does **NOT** request or access:
- Camera or microphone
- Precise GPS location
- Device storage or media gallery
- Contacts or phone state

---

## 4. User Consent and Privacy Choices (Google UMP)

In regions where user consent is legally mandated (such as the European Economic Area, the UK, and South Africa):
- We implement Google's **User Messaging Platform (UMP) SDK** before displaying personalized ads.
- Users can review, accept, or reject personalized advertising at any time.
- Users can modify their privacy choices directly within the App via **Privacy Policy → Consent Settings** or through their Android device settings under **Settings → Google → Ads → Reset/Delete Advertising ID**.

---

## 5. Data Security and Encryption
All video stream requests, API handshakes, and advertising telemetry transmissions occur over secure TLS 1.3 / HTTPS encryption in transit.

---

## 6. Children's Privacy
Mzansi TV is a general-audience television streaming app. We do not knowingly collect personal data from children under the age of 13. Public educational broadcasts (such as SABC Education) are provided purely for public education without user profiling.

---

## 7. Data Retention and Deletion
Because Mzansi TV does not operate user accounts or cloud databases, no personal identifiers are retained on our servers. To delete local preferences, users can simply clear the app's cache/data via Android system settings or uninstall the App.

---

## 8. Changes to this Privacy Policy
We may update our Privacy Policy from time to time. Any changes will be posted on this page with an updated effective date.

---

## 9. Contact Us
If you have any questions or concerns regarding this Privacy Policy or your data rights, please contact:

- **Developer:** Natty30
- **Email:** zingelwayocivilandplant@gmail.com
- **Repository:** https://github.com/Natty30/Mzansitv
