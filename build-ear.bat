@echo off
setlocal

set "PROFILE=%~1"
if "%PROFILE%"=="" set "PROFILE=websphere"
set "DEFAULT_PROJECT_DIR=C:\Users\tkwtd0\pr"
if "%PROJECT_DIR%"=="" set "PROJECT_DIR=%DEFAULT_PROJECT_DIR%"
if not "%~2"=="" set "PROJECT_DIR=%~2"

if defined JAVA_HOME_8 (
    set "JAVA_HOME=%JAVA_HOME_8%"
) else if defined JAVA8_HOME (
    set "JAVA_HOME=%JAVA8_HOME%"
)

if not exist "%PROJECT_DIR%\pom.xml" (
    echo ERROR: pom.xml not found in "%PROJECT_DIR%".
    echo Update PROJECT_DIR in this script if your checkout path is different.
    exit /b 1
)

where mvn >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not on PATH.
    exit /b 1
)

echo Building EAR in "%PROJECT_DIR%" with profile "%PROFILE%"...
if defined JAVA_HOME (
    echo Using JAVA_HOME=%JAVA_HOME%
) else (
    echo JAVA_HOME not set by script. Using java from PATH.
)

pushd "%PROJECT_DIR%" || exit /b 1
call mvn -version
call mvn clean package -P%PROFILE%
set "BUILD_RC=%ERRORLEVEL%"
popd

if not "%BUILD_RC%"=="0" (
    echo Build failed with exit code %BUILD_RC%.
    exit /b %BUILD_RC%
)

echo Build complete.
set "EAR_PATH=%PROJECT_DIR%\progreg-sec-ear\target\progreg-sec.ear"
if exist "%EAR_PATH%" (
    echo EAR: %EAR_PATH%
) else (
    echo Build succeeded, but EAR file was not found at "%EAR_PATH%".
)
exit /b 0
