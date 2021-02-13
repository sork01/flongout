#!/bin/bash

git status

echo '========================================================='
echo 'Vad har du ändrat (se ovan)? Skriv ditt meddelande under:'
read msg

git add -A
git commit -m "$msg"
git push
