#!/bin/bash
set -e

DOWNLOAD=true

if [ "$DOWNLOAD" = true ]; then
    cd react-native-pjsip-builder-2.8.0
    ./release.sh
    #cp -rf dist/ios/VialerPJSIP.framework ../ios/
    cp -rf dist/android/src/* ../android/src
fi
