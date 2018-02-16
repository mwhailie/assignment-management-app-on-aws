#!/bin/bash
#delete a stack instance
instanceId=$(aws cloudformation describe-stack-resource --stack-name $1 --logical-resource-id EC2Instance --query "StackResourceDetail.PhysicalResourceId" --output text)

#enable-api-termination
aws ec2 modify-instance-attribute --no-disable-api-termination --instance-id $instanceId

#delete the stack named mystack
aws cloudformation delete-stack --stack-name $1
