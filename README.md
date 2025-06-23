# Face Weave: Real-Time Face Mesh Detection App

**FaceWeave** is a real-time Android app that uses **Google's ML Kit** to perform on-device **face detection**, **image labeling**, and **face mesh overlay**. Built with **CameraX**, **Kotlin Coroutines**, and **Material Design**, it demonstrates a smooth ML experience on mobile using official tools only—no third-party ML libraries involved.

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

<p>
  <img src="https://github.com/manish2kumar/Face_Weave/blob/main/screenshots/Image_1.jpeg" width="220" height="450">&nbsp;
  <img src="https://github.com/manish2kumar/Face_Weave/blob/main/screenshots/Image_2.jpeg" width="220" height="450">&nbsp;
  <img src="https://github.com/manish2kumar/Face_Weave/blob/main/screenshots/Image_3.jpeg" width="220" height="450">&nbsp;
  <img src="https://github.com/manish2kumar/Face_Weave/blob/main/screenshots/Image_4.jpeg" width="220" height="450">
</p>

---

## 🛠️ Built With

- [ML Kit Face Detection](https://developers.google.com/ml-kit/vision/face-detection/android)
- [ML Kit Face Mesh (beta)](https://developers.google.com/ml-kit/vision/face-mesh-detection)
- [ML Kit Image Labeling](https://developers.google.com/ml-kit/vision/image-labeling)
- [CameraX](https://developer.android.com/training/camerax)
- Kotlin, Coroutines, ViewModel
- Material Design Components

---

## 🧪 How It Works

1. **CameraX** captures camera frames in real time.
2. Each frame is analyzed by:
   - ML Kit’s **Face Detector** to retrieve attributes and bounding boxes.
   - ML Kit’s **Image Labeler** to classify objects in the frame.
   - ML Kit’s **Face Mesh Detector** to identify facial landmarks.
3. Results are drawn on:
   - An `ImageView` (captured frame),
   - A custom `FaceMeshOverlay` (landmark sketch),
   - TextViews (probabilities and labels).

---

## 📦 Setup Instructions

To install the application, follow these steps:

- Clone this repository to your local machine.

```bash
git clone https://github.com/manish2kumar/FaceWeave.git
cd FaceWeave
```
- Open the project in Android Studio.

- Build and run the application on your Android device or emulator.
- 💡 The app requires camera and storage permissions. Grant them when prompted.
