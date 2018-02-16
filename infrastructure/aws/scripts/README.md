# scripts

## Requirement

Assortment of scripts that run on the AWS Command Line Interface (CLI). To run these script, you need to first install and configure AWS Command Line Interface (CLI).

## Description

### create-security-group.sh
Create security groups.

### delete-security-group.sh
Delete security groups.

### launch-ec2-instance.sh
- Create security group.
- Configure security group.
- Launch EC2 Instance.
- Wait for instance to be in running state.
- Retrieving instance’s public IP address.
- Add/Update type A resource record set ec2.YOUR_DOMAIN_NAME.me in the Route 53 zone for your domain with the IP of the newly launched EC2 instance. TTL 60 seconds.

To run this shell, you may have to change the key-name to your own keyname in the following line.

```
# START AN INSTANCE & GET ITS ID
INSTANCE_ID=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --count 1 --instance-type t2.micro --key-name id_rsa
```

You may also have to change the last line. Change value of "Name" to your Domain Name.

```
aws route53 change-resource-record-sets --hosted-zone-id Z3PGXZUZ3TZUH6 --change-batch "{\"Comment\":\"Update record to reflect new IP address\",\"Changes\":[{\"Action\":\"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"csye6225-fall2017-wenhe.me.\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$PUBLIC_IP\"}]}}]}"
```

### delete-security-group.sh
Terminate EC2 instance. Will take the instance-id as command line argument.

```
delete-security-group.sh <instance-id>
```

### create-csye6225-cloudformation-stack.sh
Create a CloudFormation stack that contains following resources:
- Security Group
- EC2 Instance with the specifications below
- Resource Record in the Route 53 zone for your domain with the IP of the newly launched EC2 instance

```
create-csye6225-cloudformation-stack.sh <stack-name>
```

### delete-csye6225-cloudformation-stack.sh
Terminate EC2 instance. Will take the stack name as command line argument.

```
delete-csye6225-cloudformation-stack.sh <stack-name>
```
