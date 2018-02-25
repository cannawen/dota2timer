#!/bin/bash
if [ "$TRAVIS_BRANCH" == "master" ]; then
    curl -F "status=2" -F "notify=0" -F "notes=$TRAVIS_COMMIT_MESSAGE" -F "notes_type=0" -F "commit_sha=$TRAVIS_COMMIT" -F "ipa=@app/build/outputs/apk/debug/app-debug.apk" -H "X-HockeyAppToken:$HOCKEYAPP_TOKEN" https://rink.hockeyapp.net/api/2/apps/upload
fi
