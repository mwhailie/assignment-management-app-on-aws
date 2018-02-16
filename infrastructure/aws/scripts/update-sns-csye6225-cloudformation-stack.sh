#!/bin/bash
#update the stack instance, sns
VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)
SUBNET_ID_2=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[1].SubnetId" --output text)

HOSTEDZONE_ID=$(aws route53 list-hosted-zones --query "HostedZones[0].Id" --output text)

NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)

aws cloudformation update-stack --stack-name $1 --notification-arns --template-body file://../cloudformation/update-simple-ec2-instance-securitygroup-cloudformation-stack.json --parameters "ParameterKey=ParamVPCID,ParameterValue=$VPC_ID" "ParameterKey=ParamSubnetID,ParameterValue=$SUBNET_ID" "ParameterKey=ParamHostedZoneID,ParameterValue=$HOSTEDZONE_ID" "ParameterKey=ParamRecordSetsName,ParameterValue=$NAME" "ParameterKey=ParamSubnetID2,ParameterValue=$SUBNET_ID_2" --capabilities "CAPABILITY_NAMED_IAM" 


