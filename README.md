# 📍 Offline Location Live Tracking (Android)

An **offline-first Android application** that tracks live location in the background, persists data locally, and reliably syncs it to a server when network connectivity is available.

---

## 🚀 Features

- **Foreground Location Tracking**
  - Runs as a foreground service with a persistent notification
  - Reliable background tracking (Android 14+ compliant)

- **Offline-First Architecture**
  - Location data stored locally using Room
  - Works seamlessly without internet connectivity

- **Reliable Sync Engine**
  - Uses WorkManager with network constraints
  - Automatically syncs when device comes online
  - FIFO (First-In-First-Out) upload order

- **Dynamic User Context**
  - `employeeId` persisted using DataStore
  - Injected dynamically into upload payloads
  - Avoids duplication in database schema

- **Sync Status Tracking**
  - Each location record has an `isSynced` flag
  - Pending logs counter displayed in UI

- **Network Awareness**
  - Online / Offline indicator in UI
  - Sync triggered only when network is available

- **API Upload Verification**
  - Uploads tested using a real HTTP POST to `webhook.site`
  - Request payloads verified externally

---

## 🧱 Tech Stack

- Kotlin  
- Jetpack Compose  
- Foreground Service  
- Fused Location Provider  
- Room (local persistence)  
- WorkManager (background sync)  
- DataStore (persistent user data)  
- OkHttp (network calls)  
- Webhook.site (API verification)

---
## 🗂️ Key Components

### LocationTrackingService
- Runs as a foreground service
- Requests periodic location updates
- Saves each location to Room with `isSynced = false`

### Room Database
- Stores all location logs locally
- Supports querying unsynced records
- Maintains sync state per record

### Sync Engine (WorkManager)
- Triggered when network is available
- Uploads unsynced logs in FIFO order
- Marks logs as synced after upload

### DataStore (UserPreferences)
- Stores `employeeId` persistently
- Read once per sync cycle
- Injected into upload payload dynamically

---

## 🔁 Sync Flow (Offline → Online)

1. User starts tracking
2. Locations are stored locally (offline supported)
3. Network becomes available
4. WorkManager triggers sync
5. Unsynced logs are uploaded in FIFO order
6. Sync status is updated in database

---

## 🌐 API Upload Verification

Uploads were verified using **Webhook.site** via real HTTP POST requests.

Example payload:

```json
{
  "employeeId": "EMP001",
  "latitude": 28.61,
  "longitude": 77.20,
  "accuracy": 9.5,
  "timestamp": 1712230429000,
  "speed": 1.2
}
```
## 📦 APK Download

- [Download Debug APK] (https://github.com/Shubhanshi04/offline-location-tracking-android/releases/tag/v1.0)

