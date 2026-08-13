@echo off
REM Complete Gradle and build cache cleanup for Windows

echo Stopping Gradle daemon...
call gradlew --stop

echo Waiting 3 seconds...
timeout /t 3 /nobreak

echo Deleting .gradle directory...
if exist .gradle rmdir /s /q .gradle
if exist .gradle echo WARNING: Could not delete .gradle - may be locked

echo Deleting app/build directory...
if exist app\build rmdir /s /q app\build
if exist app\build echo WARNING: Could not delete app\build - may be locked

echo Deleting terminal-emulator/build directory...
if exist termux-kotlin-app\terminal-emulator\build rmdir /s /q termux-kotlin-app\terminal-emulator\build

echo Deleting terminal-view/build directory...
if exist termux-kotlin-app\terminal-view\build rmdir /s /q termux-kotlin-app\terminal-view\build

echo Deleting termux-shared/build directory...
if exist termux-kotlin-app\termux-shared\build rmdir /s /q termux-kotlin-app\termux-shared\build

echo Deleting build directory in root...
if exist build rmdir /s /q build

echo Clearing Kotlin incremental compiler cache...
if exist app\build\kotlin rmdir /s /q app\build\kotlin

echo.
echo Cleanup complete!
echo.
echo Now running clean build...
echo.

call gradlew clean build

echo.
echo Build complete! Check output above for success/failure.
echo.
pause
