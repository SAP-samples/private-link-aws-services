# AWS RDS Data API PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-privatelink -c '{"serviceName": "com.amazonaws.eu-central-1.rds-data"}'
```

## Create Aurora Serverless v1, enable the data API and create secrets

Create an Aurora Serverless v1 MySQL cluster and follow the [official documentation](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/data-api.html#data-api.access)
to prepare the data API.

When finished, note down the cluster resource arn (e.g. `arn:aws:rds:us-east-1:123456789012:cluster:my-aurora`) 
and the ARN of the secret created (e.g. `arn:aws:secretsmanager:us-east-1:123456789012:secret:path/to/my-aurora-secret`)

## Create a user-provided service

Create a user-provided service to provide your AWS credentials as well as your RDS configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"rdsArn": "<rdsArn>", "secretArn": "<secretArn>", "accessKeyId": "<accessKeyId>", "secretAccessKey": "<secretAccessKey>", "region": "<awsRegion>"}'
```

## Build and push the application

Build the application and push it to CloudFoundry:

```bash
./mvnw package
cf push
```

The `cf push` command will automatically bind the Private Link service instance and the user-provided service to the pushed application
as defined in the [manifest file](manifest.yml).

**Note: Be aware that the pushed application is publicly accessible via the provided route and should therefore be removed after testing.**

## Query the endpoint to create a database

Retrieve the automatically registered route of your application from the output of running `cf apps`.
Use curl to create a database, the output of the application will show some logs.

```bash
curl -X POST 'https://<route>/database'

# View the Cloud Foundry application logs
cf logs rds-data-pls-demo --recent
```