# cloudformation

## Description

This is the template to deploy amazon couldformation. This json file is passed as a parameter to create stack.

This template contains:

* EC2 Instance
* Resource Record in the Route 53 zone for your domain with the IP of the newly launched EC2 instance
* Security Group for EC2 Instances
* Security Group for RDS Instances
* DynamoDB Table