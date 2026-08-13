#!/bin/bash
# Local Build Script for Machina Android App with Termux Terminal

set -e

echo "=========================================="
echo "Machina Android App - Local Build"
echo "=========================================="
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java 17+ is required but not installed"
    exit 1
fi

echo "✓ Java available: $(java -version 2>&1 | grep version)"
echo ""

# Check gradlew
if [ ! -f "./gradlew" ]; then
    echo "❌ Gradle wrapper not found"
    exit 1
fi

echo "🔨 Building APK (this may take 5-15 minutes)..."
echo ""

# Run gradle build
./gradlew clean assembleDebug -x test \
    -Dorg.gradle.jvmargs="-Xmx4g" \
    -Dorg.gradle.parallel=true \
    --build-cache

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

if [ -f "$APK_PATH" ]; then
    APK_SIZE=$(ls -lh "$APK_PATH" | awk '{print $5}')
    echo ""
    echo "=========================================="
    echo "✓ Build Complete!"
    echo "=========================================="
    echo "APK Location: $APK_PATH"
    echo "APK Size: $APK_SIZE"
    echo ""
    echo "Next steps:"
    echo "  1. Connect your Android device via USB"
    echo "  2. Run: adb install -r $APK_PATH"
    echo "  3. Launch 'Machina' app from your device"
    echo "=========================================="
else
    echo ""
    echo "❌ Build failed - APK not found at $APK_PATH"
    exit 1
fi
