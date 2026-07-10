#!/bin/sh
set -e

# Many PaaS hosts (Railway, Render, Heroku) inject the listen port via $PORT.
# Rewrite Tomcat's HTTP connector to use it; fall back to 8080 locally.
PORT="${PORT:-8080}"
sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml

exec "$@"
