# AWS RDS Data API PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create an RDS Data API VPC Endpoint
Create a VPC Endpoint to the service `rds-data` eg. `com.amazonaws.us-east-1.rds-data`.
Note the hostname, it should look similar to `vpce-00000000000000000-00000000.rds-data.us-east-1.vpce.amazonaws.com`.

If using the SAP Private Link service, create a service instance using the following command:
```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink standard my-service-instance-name -c '{"serviceName": "com.amazonaws.eu-central-1.rds-data"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create Aurora Serverless v1, enable the data API and create secrets
Create an Aurora Serverless v1 MySQL cluster and follow the [official documentation](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/data-api.html#data-api.access) to prepare 
to prepare the data API.

When finished, note down the cluster resource arn (e.g. `arn:aws:rds:us-east-1:123456789012:cluster:my-aurora`) 
and the ARN of the secret created (e.g. `arn:aws:secretsmanager:us-east-1:123456789012:secret:path/to/my-aurora-secret`)

## Run the application

```bash
RDS_ENDPOINT_URL="<url>" \
RDS_SECRET_ARN="<secret-manager-secret-arn>" \
RDS_RESOURCE_ARN="<rds-resource-arn>" \
AWS_ACCESS_KEY_ID="<access_key_id>" \
AWS_SECRET_ACCESS_KEY="<secret_access_key>" \
./mvnw spring-boot:run
```

## Query the endpoint to create a database
Use curl to create a database, the output of the application will show some logs
```bash
curl -X POST '127.0.0.1:8080/database'
```