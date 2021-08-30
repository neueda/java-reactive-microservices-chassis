#!/usr/bin/env bash

ab -c 230 -n 100000 -r http://localhost:8080/api/v1/chassis | tee "$1"
