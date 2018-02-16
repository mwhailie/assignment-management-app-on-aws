#!/bin/bash
cd /home/ubuntu
wget https://s3.amazonaws.com/aws-cloudwatch/downloads/latest/awslogs-agent-setup.py
sudo python ./awslogs-agent-setup.py -n -r us-east-1 --region us-east-1 -c ./awslogs.conf
sudo systemctl enable awslogs.service
sudo service awslogs start
