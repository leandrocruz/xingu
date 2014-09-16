#! /bin/sh

url='http://localhost:8080'

curl -i -k -3 \
"$url" \
-F arquivo=@/home/leandro/modelo-vinculacao.xls \
-F "campo=Cópia Integral é nóis;type=utf8" \
--compressed
