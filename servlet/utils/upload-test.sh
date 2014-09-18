#! /bin/sh

url='http://localhost:8080'

curl -i -k -3 \
"$url" \
-F arquivo=@/home/leandro/Documents/Lessig-Codev2.pdf \
-F "campo=Cópia Integral;type=utf8" \
--compressed
