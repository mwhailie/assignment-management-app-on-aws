instanceName=$(gcloud sql instances list --format 'value(NAME)')

gcloud beta sql databases create DATABASE1 --instance=$instanceName