#!/bin/bash
set -e

chmod +x ./gradlew
./gradlew :app:dependencies --configuration runtimeClasspath --no-daemon --console=plain
./gradlew :app:dependencies --configuration testRuntimeClasspath --no-daemon --console=plain
