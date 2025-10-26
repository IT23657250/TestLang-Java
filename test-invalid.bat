@echo off
REM Script to test the compiler with invalid input

echo ========================================
echo Testing Error Handling
echo ========================================
echo.

java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar invalid.test

echo.
echo Press any key to continue...
pause >nul
