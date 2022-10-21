#!/bin/bash

export CURRENT_BRANCH=$(git branch --show-current)
echo "current branch=$CURRENT_BRANCH"

git push -u origin $CURRENT_BRANCH

read -p "Cmd-click or ctrl-click the URL and post your PR and then in this shell press enter to continue"

git checkout main

git branch -D $CURRENT_BRANCH

git pull
