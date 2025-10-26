@echo off
REM Script to compile a .test file to Java

if "%~1"=="" (
    echo Usage: compile-test.bat ^<input.test^> [output.java]
    echo.
    echo Example: compile-test.bat example.test GeneratedTests.java
    exit /b 1
)

set INPUT=%~1
set OUTPUT=%~2

if "%OUTPUT%"=="" (
    set OUTPUT=GeneratedTests.java
)

echo ========================================
echo Compiling TestLang++ file
echo ========================================
echo Input:  %INPUT%
echo Output: %OUTPUT%
echo.

java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar %INPUT% %OUTPUT%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Compilation failed!
    echo ========================================
    exit /b 1
)
