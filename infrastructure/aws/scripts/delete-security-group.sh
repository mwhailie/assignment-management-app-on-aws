#!/bin/bash
#delete a security group

if  [ -n "$(aws ec2 describe-security-groups --group-names "csye6225-fall2017-webapp" 2>/dev/null)" ]; then
aws ec2 delete-security-group --group-name "csye6225-fall2017-webapp"
fi
