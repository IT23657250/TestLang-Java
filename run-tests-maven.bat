@echo off
REM Alternative test runner using Maven directly

echo ========================================
echo Running Generated Tests with Maven
echo ========================================
echo.
echo Make sure the backend is running on http://localhost:8080
echo.

REM Create a temporary POM for running the tests
echo Creating temporary test project...

REM Create test directory structure
if not exist "test-project\src\test\java" mkdir test-project\src\test\java

REM Copy generated test
copy GeneratedTests.java test-project\src\test\java\ >nul

REM Create minimal POM
(
echo ^<?xml version="1.0" encoding="UTF-8"?^>
echo ^<project xmlns="http://maven.apache.org/POM/4.0.0"
echo          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
echo          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"^>
echo     ^<modelVersion^>4.0.0^</modelVersion^>
echo     ^<groupId^>com.testlang^</groupId^>
echo     ^<artifactId^>generated-tests^</artifactId^>
echo     ^<version^>1.0^</version^>
echo     ^<properties^>
echo         ^<maven.compiler.source^>11^</maven.compiler.source^>
echo         ^<maven.compiler.target^>11^</maven.compiler.target^>
echo     ^</properties^>
echo     ^<dependencies^>
echo         ^<dependency^>
echo             ^<groupId^>org.junit.jupiter^</groupId^>
echo             ^<artifactId^>junit-jupiter-api^</artifactId^>
echo             ^<version^>5.9.3^</version^>
echo             ^<scope^>test^</scope^>
echo         ^</dependency^>
echo         ^<dependency^>
echo             ^<groupId^>org.junit.jupiter^</groupId^>
echo             ^<artifactId^>junit-jupiter-engine^</artifactId^>
echo             ^<version^>5.9.3^</version^>
echo             ^<scope^>test^</scope^>
echo         ^</dependency^>
echo     ^</dependencies^>
echo     ^<build^>
echo         ^<plugins^>
echo             ^<plugin^>
echo                 ^<groupId^>org.apache.maven.plugins^</groupId^>
echo                 ^<artifactId^>maven-surefire-plugin^</artifactId^>
echo                 ^<version^>3.0.0^</version^>
echo             ^</plugin^>
echo         ^</plugins^>
echo     ^</build^>
echo ^</project^>
) > test-project\pom.xml

REM Run the tests
cd test-project
echo Running tests...
call mvn test
cd ..

echo.
echo ========================================
echo Test execution complete
echo ========================================
echo.
echo To clean up: rmdir /s /q test-project
