#!/bin/bash
#terminate a EC2 instance

#terminate running instance with argument
aws ec2 modify-instance-attribute --no-disable-api-termination --instance-id $1
aws ec2 terminate-instances --instance-ids  $1

#wait for terminated
aws ec2 wait instance-terminated

#delete a security group

if  [ -n "$(aws ec2 describe-security-groups --group-names "csye6225-fall2017-webapp" 2>/dev/null)" ]; then
aws ec2 delete-security-group --group-name "csye6225-fall2017-webapp"
fi
