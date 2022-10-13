#!/bin/bash
BASE_DIR=$1
WORK_DIR="ip"
cd $BASE_DIR
rm -rf $WORK_DIR
git clone git@github.com:KehwZwei/ip.git