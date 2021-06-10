#!/usr/bin/env bash

mvn clean package -f pom.xml
ssh XSheep@43.129.25.210 "cd projects/CodeCraft-SpeedCode/Backend-Server && ./stop.sh"
scp -r target/*.jar XSheep@43.129.25.210:/home/XSheep/projects/CodeCraft-SpeedCode/Backend-Server
ssh XSheep@43.129.25.210 "cd projects/CodeCraft-SpeedCode/Backend-Server && ./start.sh"