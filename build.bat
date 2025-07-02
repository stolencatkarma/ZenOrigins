@echo off
echo Building ZenOrigins...
call mvn clean package
if %ERRORLEVEL% EQU 0 (
    echo Build successful! JAR file is located in target/ZenOrigins-1.0.0.jar
) else (
    echo Build failed! Check the output above for errors.
)
pause
