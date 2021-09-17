#!/bin/bash

FILES="./*.yaml"
for f in $FILES
do
  echo "Processing $f file..."
  kubectl delete -f "$f"
done