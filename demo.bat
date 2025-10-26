@echo off
REM Complete end-to-end workflow

echo ========================================
echo TestLang++ End-to-End Workflow
echo ========================================
echo.

echo Step 1: Building compiler...
call build.bat
if %ERRORLEVEL% NEQ 0 exit /b 1

echo.
echo Step 2: Compiling example.test...
call compile-test.bat example.test GeneratedTests.java
if %ERRORLEVEL% NEQ 0 exit /b 1

echo.
echo Step 3: Ready to run tests!
echo.
echo Next steps:
echo   1. Open a new terminal and run: run-backend.bat
echo   2. Wait for backend to start (you'll see "Started TestLangBackendApplication")
echo   3. Run: run-tests.bat
echo.
echo Or test with invalid input:
echo   compile-test.bat invalid.test
echo.

pause
