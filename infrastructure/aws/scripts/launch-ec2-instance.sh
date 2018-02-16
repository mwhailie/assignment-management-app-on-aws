#!/bin/sh
# CREATE SECURITY GROUP
VpcId=$(aws ec2 describe-vpcs | jq -r '.Vpcs[0].VpcId')
if  [ -z "$(aws ec2 describe-security-groups --group-names "csye6225-fall2017-webapp" 2>/dev/null)" ]; then
aws ec2 create-security-group --group-name "csye6225-fall2017-webapp" --description "csye security group" --vpc-id $VpcId
fi

GROUP_ID=$(aws ec2 describe-security-groups --group-name "csye6225-fall2017-webapp" | jq -r '.SecurityGroups[0].GroupId')
# START AN INSTANCE & GET ITS ID
INSTANCE_ID=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --count 1 --instance-type t2.micro --key-name MyKeyPairs --security-group-ids $GROUP_ID --disable-api-termination | jq -r '.Instances[0].InstanceId')
aws ec2 authorize-security-group-ingress --group-id  $GROUP_ID --protocol tcp --port 22 --cidr 203.0.113.1/32
aws ec2 authorize-security-group-ingress --group-id  $GROUP_ID --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id  $GROUP_ID --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 wait instance-running --instance-id $INSTANCE_ID # wait running

# VOLUMES CONFIGURE
VOLUMES_ID=$(aws ec2 describe-volumes --filters Name=attachment.instance-id,Values=$INSTANCE_ID | jq -r '.Volumes[0].VolumeId')
echo $VOLUMES_ID
aws ec2 modify-volume --volume-id $VOLUMES_ID --size 16 --volume-type gp2

# PUBLIC IP
PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID | jq -r '.Reservations[0].Instances[0].PublicIpAddress')

# Record Set
<<<<<<< HEAD:infrastructure/scripts/launch-ec2-instance.sh
<<<<<<< HEAD
aws route53 change-resource-record-sets --hosted-zone-id ZB1V1M89K31BX --change-batch "{\"Comment\":\"Update record to reflect new IP address\",\"Changes\":[{\"Action\":\"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"ec2.csye6225-fall2017-jingyu.me.\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$PUBLIC_IP\"}]}}]}"
=======
aws route53 change-resource-record-sets --hosted-zone-id Z1SY1R2IE2WCBI --change-batch "{\"Comment\":\"Update record to reflect new IP address\",\"Changes\":[{\"Action\":\"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"csye6225-fall2017-yanhao.me.\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$PUBLIC_IP\"}]}}]}"
>>>>>>> 7e969c5cd1ef3cdef602ff028e61db87ba3f0747
=======
aws route53 change-resource-record-sets --hosted-zone-id Z1SY1R2IE2WCBI --change-batch "{\"Comment\":\"Update record to reflect new IP address\",\"Changes\":[{\"Action\":\"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"ec2.csye6225-fall2017-yanhao.me.\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$PUBLIC_IP\"}]}}]}"
>>>>>>> 1595197ad800cdf9415045317487cf8fc0c941c7:infrastructure/aws/scripts/launch-ec2-instance.sh
