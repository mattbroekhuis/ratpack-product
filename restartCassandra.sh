#!/usr/bin/env bash
docker rm -f cassandra || true
docker run -d --name cassandra  -p 9042:9042 cassandra:2.2.3

until nc -w 1 192.168.99.100 9042
do
    sleep 2
    echo "$(date) - waiting for cassandra to start...."
done

echo "$(date) - connected successfully, applying scripts"

docker cp setup.cql cassandra:/setup.cql

docker exec  cassandra cqlsh -f /setup.cql