@echo off
REM Build and run the backend server

echo ========================================
echo Building Backend Server
echo ========================================
echo.

cd backend

echo Cleaning and building...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    cd ..
    exit /b 1
)

echo.
echo ========================================
echo Starting Backend Server
echo ========================================
echo Server will run on http://localhost:8080
echo Press Ctrl+C to stop the server
echo.

java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar

cd ..
