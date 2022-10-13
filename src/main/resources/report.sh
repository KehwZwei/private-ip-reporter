#!/bin/bash
WORK_DIR="ip"
BASE_DIR=$1
END_POINT=$2
IP=$3

cd "$BASE_DIR/$WORK_DIR"

echo $IP > $END_POINT

git add .
git commit -m "update $END_POINT ip"
git push origin