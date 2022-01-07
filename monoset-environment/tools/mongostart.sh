#!/usr/bin/env bash

echo ""
echo ""
echo "To exit mongo service:"
echo ""
echo "use admin"
echo "db.shutdownServer()"
echo ""
echo ""
echo "dbPath: /usr/local/var/mongodb"
echo "logPath: /usr/local/var/mongodb/mongo.log"
echo ""
echo ""

echo admin | sudo mongod --fork --logpath /usr/local/var/mongodb/mongo.log --dbpath /usr/local/var/mongodb
mongo
