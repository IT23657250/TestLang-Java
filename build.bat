@echo off
REM Build script for TestLang++ compiler

echo ========================================
echo Building TestLang++ Compiler
echo ========================================
echo.

REM Clean and build the project
echo Cleaning previous build...
call mvn clean

echo.
echo Building with Maven...
call mvn package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo JAR file: target\testlang-java-1.0.0-jar-with-dependencies.jar
    echo.
) else (
    echo.
    echo ========================================
    echo Build failed!
    echo ========================================
    exit /b 1
)
