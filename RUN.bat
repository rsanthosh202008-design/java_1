@echo off
setlocal
cd /d "%~dp0"
where javac >nul 2>&1
if errorlevel 1 (
  echo JDK 17 or newer is required. Install a JDK and add it to PATH.
  pause
  exit /b 1
)
if not exist out mkdir out
echo Compiling Streamify Pro...
javac -encoding UTF-8 -d out src\streamify\*.java
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
java -cp out streamify.Main
if errorlevel 1 pause
endlocal
