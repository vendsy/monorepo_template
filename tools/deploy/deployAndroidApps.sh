#!/bin/bash

#on any error from any command exit and fail the entire script instead of continuing on..
set -e

export VERSION_NUM=$1
#Type should be release or main branch so releases go to one bucket and main branch builds are dev builds going to a dev bucket
export TYPE=$2
export CIRCLE_BRANCH=$3

if [ "$CIRCLE_BRANCH" = "main" ]; then
    #USE the mainbranch AWS account
    echo "Using mainbranch AWS keys for version ${APP_VERSION}"
    export AWS_ACCESS_KEY_ID=${main_access_key_id}
    export AWS_SECRET_ACCESS_KEY=${main_secret_access_key}
else
    #USE the releasebranch AWS account
    echo "Using releasebranch AWS keys for version ${APP_VERSION}"
    export AWS_ACCESS_KEY_ID=${release_access_key_id}
    export AWS_SECRET_ACCESS_KEY=${release_secret_access_key}
fi
export CIRCLE_TOKEN=${circleCIApiToken}

# Set dash as the delimiter
IFS='-'
#Read the split words into an array based on space delimiter
read -a versionArray <<< "$VERSION_NUM"
unset IFS


export INFLATED_BUILD_NUM=${versionArray[1]}
export BUILD_NUM=$((INFLATED_BUILD_NUM - 500000))

export URL="https://circleci.com/api/v1.1/project/github/vendsy/monorepo/${BUILD_NUM}/artifacts"
echo "Finding artifacts at '${URL}'"
echo ""

mkdir -p downloads
cd downloads

curl --fail -o result.json "${URL}" -H "Circle-Token: $CIRCLE_TOKEN"

#I need this up fast so I am hacking json parsing in bash :( 
grep -nr "url" result.json > hackyJson.txt

echo "" > artifactUrls.txt 
while read LINE; do
  #echo "$LINE"
  eval "array=($LINE)"
  export FILE_NAME=${array[3]%?}
 
  if [[ $FILE_NAME == *.apk ]] 
  then
      echo "Found APK file=$FILE_NAME"
      echo $FILE_NAME >> artifactUrls.txt
  else
      echo "Skipping non-APK file=$FILE_NAME"
  fi

done <hackyJson.txt

wget --header="Circle-Token: $CIRCLE_TOKEN" -i artifactUrls.txt

for apk in *.apk; do
    echo "Uploading $apk to S3"
    aws s3 cp $apk s3://put-your-s3-bucket-here-${TYPE}-releases
done
