# Dockerfile for Android Machina App with Termux Terminal Integration
# Multi-stage build: Android build environment + local development tools

# Stage 1: Android Build Environment
FROM ubuntu:24.04 as android-builder

# Install Java and Android SDK tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    openjdk-17-jdk-headless \
    wget \
    unzip \
    git \
    curl \
    build-essential \
    cmake \
    ninja-build \
    && rm -rf /var/lib/apt/lists/*

# Set Java home
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Install Android SDK
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools/bin:$PATH

RUN mkdir -p $ANDROID_HOME && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip -q commandlinetools-linux-11076708_latest.zip -d $ANDROID_HOME && \
    rm commandlinetools-linux-11076708_latest.zip && \
    mkdir -p $ANDROID_HOME/cmdline-tools/latest && \
    mv $ANDROID_HOME/cmdline-tools/* $ANDROID_HOME/cmdline-tools/latest/ 2>/dev/null || true

# Install Android SDK components (non-interactive)
RUN yes | sdkmanager --licenses > /dev/null 2>&1 && \
    sdkmanager \
    "platforms;android-35" \
    "build-tools;35.0.0" \
    "ndk;26.2.11394342" \
    "cmake;3.22.1" \
    > /dev/null 2>&1

# Copy project
WORKDIR /build
COPY . .

# Grant execute permissions to gradle wrapper
RUN chmod +x gradlew

# Build the APK (debug)
RUN ./gradlew clean assembleDebug -x test --no-daemon \
    -Dorg.gradle.jvmargs="-Xmx4g" \
    -Dorg.gradle.parallel=true \
    --build-cache

# Stage 2: Runtime/Distribution
FROM ubuntu:24.04

# Install minimal runtime dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jre \
    adb \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copy APK from builder
COPY --from=android-builder /build/app/build/outputs/apk/debug/app-debug.apk /app/machina.apk

# Set working directory
WORKDIR /app

# Metadata
LABEL description="Machina Android App with Termux Terminal Integration" \
      version="1.0" \
      maintainer="Machina Team"

# Default command: show APK path
CMD ["bash", "-c", "echo 'APK ready at /app/machina.apk' && ls -lh machina.apk"]
