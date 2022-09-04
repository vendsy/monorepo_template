#!/bin/sh -e

# This script is run by `monobuild` from the project's base directory

./gradlew build "$@"
