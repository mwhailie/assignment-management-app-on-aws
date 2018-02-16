#!/bin/bash
#create a security group

VpcId=$(aws ec2 describe-vpcs | jq -r '.Vpcs[0].VpcId')

if  [ -z "$(aws ec2 describe-security-groups --group-names "csye6225-fall2017-webapp" 2>/dev/null)" ]; then
aws ec2 create-security-group --group-name "csye6225-fall2017-webapp" --description "csye security group" --vpc-id $VpcId
fi
