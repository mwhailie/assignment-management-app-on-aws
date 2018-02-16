#!/bin/bash
#create a deployment manager on google could platform

projectID=$(gcloud config list --format 'value(core.project)')

gcloud deployment-manager deployments update mst --config ../configuration/vm.yaml 



# gcloud deployment-manager deployments list 
