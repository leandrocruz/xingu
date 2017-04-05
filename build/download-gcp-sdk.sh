#!/bin/bash

echo "home is: '${HOME}"
SDK_VERSION=127.0.0
SDK_FILENAME=google-cloud-sdk-${SDK_VERSION}-linux-x86_64.tar.gz
curl -O -J https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/${SDK_FILENAME}
tar -zxvf ${SDK_FILENAME} --directory ${HOME}
${HOME}/google-cloud-sdk/bin/gcloud version
