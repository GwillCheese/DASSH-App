# DASSH Fitness Tracker

A comprehensive Android fitness tracking application built with Kotlin, integrating real-time sensor data and OpenStreetMap for route visualization.

## 🏃‍♂️ Features

### Core Tracking
- **⏱️ Precision Stopwatch** - Accurate timing for workouts
- **👟 Step Counter** - Real-time step detection using accelerometer sensors
- **📏 Distance Calculator** - GPS-based distance tracking with high precision
- **⚡ Speed Calculator** - Real-time average speed calculation

### Map Integration
- **🗺️ Route Visualization** - Live path drawing on OpenStreetMap tiles
- **📍 GPS Tracking** - Continuous location monitoring
- **🎯 Auto-centering** - Map follows user movement

### Architecture
- **🏗️ Modern Android Architecture** - Fragment-based with Navigation Component
- **🔧 Clean Code Structure** - Separated concerns with manager classes
- **📱 Professional UI** - Material Design with responsive layouts

## 🛠️ Technologies

- **Kotlin** - Primary development language
- **Android Studio** - Development environment
- **OpenStreetMap (OSMDroid)** - Map tiles and visualization
- **Android Sensors** - Accelerometer for step detection
- **GPS/Location Services** - Position tracking
- **Navigation Component** - Fragment navigation
- **Material Design** - UI components

## 📱 Screens

### Welcome Screen
- Professional landing page
- Feature overview
- Navigation to timer

### Timer Screen
- Stopwatch controls (Start/Pause/Reset)
- Real-time metrics display:
  - Step count
  - Distance traveled (km)
  - Average speed (km/h)
- Interactive map with route visualization

## 🏗️ Architecture

```
├── MainActivity.kt          - Navigation host
├── TimerFragment.kt         - Main tracking interface
├── WelcomeFragment.kt       - Landing screen
├── StopwatchManager.kt       - Timer logic
├── AccelerometerManager.kt   - Sensor handling
├── LocationManager.kt        - GPS management
├── MapManager.kt           - Map functionality
├── StepDetector.kt          - Step detection algorithm
├── DistanceCalculator.kt     - Distance calculations
└── SpeedCalculator.kt       - Speed calculations
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21 or higher
- Kotlin 1.8+

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on Android device or emulator

### Permissions Required
- `ACCESS_FINE_LOCATION` - GPS tracking
- `ACCESS_COARSE_LOCATION` - Approximate location
- `INTERNET` - Map tile loading

## 📊 How It Works

### Step Detection
- Uses linear acceleration sensor
- Peak detection algorithm for accuracy
- Real-time step counting

### Distance Calculation
- GPS coordinate tracking
- Haversine formula for precise distance
- Continuous distance accumulation

### Route Visualization
- OpenStreetMap tiles integration
- Real-time polyline drawing
- Red path shows traveled route

## 🎯 Use Cases

- **Running/Jogging** - Track distance and pace
- **Walking** - Monitor steps and route
- **Cycling** - GPS-based distance tracking

