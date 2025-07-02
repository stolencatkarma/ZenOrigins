#!/bin/bash
echo "Building ZenOrigins..."
mvn clean package
if [ $? -eq 0 ]; then
    echo "Build successful! JAR file is located in target/ZenOrigins-1.0.0.jar"
else
    echo "Build failed! Check the output above for errors."
fi
