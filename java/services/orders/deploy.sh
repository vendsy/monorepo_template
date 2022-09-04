#!/bin/sh -e

# This script is run by `monobuild deploy` from the project's base directory

./gradlew jib "$@"
