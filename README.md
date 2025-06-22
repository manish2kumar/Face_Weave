# FaceWeave: Real-Time Face Mesh Detection App

FaceWeave is a real-time Android application that demonstrates on-device face detection, image labeling, and face mesh overlay using **Google's ML Kit**. Built with **CameraX**, **Kotlin Coroutines**, and **Material Design**, the app showcases how to create a performant, camera-based ML experience without relying on third-party libraries.

---

## 🚀 Features

- 🎯 **Real-time Face Mesh Detection**  
  Draws 468 facial landmarks using ML Kit's Face Mesh APIs with high accuracy and stability.

- 🧠 **On-device Image Labeling**  
  Detects objects and scene context live from the camera preview using ML Kit Image Labeling.

- 🧍‍♂️ **Face Detection with Attributes**  
  Identifies facial contours, head Euler angles, and tracking ID in real time.

- ⚡ **Optimized Performance**  
  Achieves up to **60 FPS** and under **100ms rendering latency** on mid-range devices using lifecycle-aware threading.

- 🎬 **Animated Splash Screen**  
  Uses `Theme.SplashScreen` API with vector assets—no extra XML layout required—for fast and modern UX.

---

## 📸 Screenshots

| Face Mesh Overlay | Real-time Labeling | Splash Screen |
|-------------------|--------------------|---------------|
| ![mesh](./screenshots/face_mesh.png) | ![labeling](./screenshots/image_labeling.png) | ![splash](./screenshots/splash.png) |

---

## 🛠️ Built With

- [ML Kit](https://developers.google.com/ml-kit)
- [CameraX](https://developer.android.com/training/camerax)
- Kotlin Coroutines
- Android Jetpack (ViewModel, Lifecycle)
- Material Components

---

## 🧪 How It Works

1. **CameraX** captures frames and feeds them to ML Kit processors.
2. ML Kit performs:
   - Face Detection with landmark and contour extraction.
   - Image Labeling from preview frames.
   - Face Mesh landmark extraction for overlay.
3. Results are drawn live on a custom `OverlayView`.

---

## 📦 Setup Instructions

To install the application, follow these steps:

- Clone this repository to your local machine.

```bash
git clone https://github.com/yourusername/FaceWeave.git
cd FaceWeave
```
- Open the project in Android Studio.

- Build and run the application on your Android device or emulator.
