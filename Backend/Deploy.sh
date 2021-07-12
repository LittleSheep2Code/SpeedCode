ssh XSheep@43.129.25.210 "cd /home/XSheep/projects/CodeCraft-SpeedCode/Backend-Server && ./stop.sh"
scp -r $(ls . | grep -v resource/source.sqlite | xargs) XSheep@43.129.25.210:/home/XSheep/projects/CodeCraft-SpeedCode/Backend-Server
ssh XSheep@43.129.25.210 "cd /home/XSheep/projects/CodeCraft-SpeedCode/Backend-Server && ./start-nohup.sh"