@echo off
setlocal
cd /d "%~dp0"
echo Preparando Gradle 8.9...
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\preparar-gradle.ps1"
if errorlevel 1 (echo Falha ao preparar Gradle. & pause & exit /b 1)
call "%~dp0.gradle-portable\gradle-8.9\bin\gradle.bat" assembleDebug
if errorlevel 1 (echo Falha no build. Verifique JDK 17 e Android SDK 35. & pause & exit /b 1)
echo APK gerado em app\build\outputs\apk\debug\app-debug.apk
pause
