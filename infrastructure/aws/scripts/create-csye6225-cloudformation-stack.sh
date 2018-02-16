#!/bin/bash
#create a stack instance

VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)
SUBNET_ID_2=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[1].SubnetId" --output text)

HOSTEDZONE_ID=$(aws route53 list-hosted-zones --query "HostedZones[0].Id" --output text)

NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)

CERTIFICATE_ARN=$(aws iam list-server-certificates --query "ServerCertificateMetadataList[0].Arn" --output text)

aws cloudformation create-stack --stack-name $1 --template-body file://../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json --parameters "ParameterKey=ParamVPCID,ParameterValue=$VPC_ID" "ParameterKey=ParamSubnetID,ParameterValue=$SUBNET_ID" "ParameterKey=ParamHostedZoneID,ParameterValue=$HOSTEDZONE_ID" "ParameterKey=ParamRecordSetsName,ParameterValue=$NAME" "ParameterKey=ParamSubnetID2,ParameterValue=$SUBNET_ID_2" "ParameterKey=ParamCertificateArn,ParameterValue=$CERTIFICATE_ARN" --capabilities "CAPABILITY_NAMED_IAM" 
