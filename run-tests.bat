@echo off
REM Script to compile and run the generated JUnit tests

echo ========================================
echo Compiling Generated Tests
echo ========================================
echo.

REM Create output directory
if not exist "test-output" mkdir test-output

REM Compile the generated test (JUnit API is in the classpath from Maven dependencies)
echo Compiling GeneratedTests.java...
call mvn dependency:copy-dependencies -DoutputDirectory=lib -q

echo Compiling GeneratedTests.java with JUnit...
javac -cp "lib\*" -d test-output GeneratedTests.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    exit /b 1
)

echo Compilation successful!

echo.
echo ========================================
echo Running Tests
echo ========================================
echo Make sure the backend is running on http://localhost:8080
echo.

REM Download JUnit standalone if not present
if not exist "lib\junit-platform-console-standalone.jar" (
    echo Downloading JUnit Platform Console Standalone...
    if not exist "lib" mkdir lib
    call mvn dependency:get -Dartifact=org.junit.platform:junit-platform-console-standalone:1.9.3:jar -Dtransitive=false 2>nul
    copy "%USERPROFILE%\.m2\repository\org\junit\platform\junit-platform-console-standalone\1.9.3\junit-platform-console-standalone-1.9.3.jar" lib\junit-platform-console-standalone.jar >nul
)

REM Check if JAR exists
if not exist "lib\junit-platform-console-standalone.jar" (
    echo ERROR: Could not download JUnit standalone JAR.
    echo Please run this command manually:
    echo   mvn dependency:get -Dartifact=org.junit.platform:junit-platform-console-standalone:1.9.3:jar
    echo   copy "%%USERPROFILE%%\.m2\repository\org\junit\platform\junit-platform-console-standalone\1.9.3\junit-platform-console-standalone-1.9.3.jar" lib\junit-platform-console-standalone.jar
    exit /b 1
)

REM Run the tests using JUnit Platform Console Launcher
java -jar lib\junit-platform-console-standalone.jar --class-path test-output --scan-class-path

echo.
echo ========================================
echo Test execution complete
echo ========================================
