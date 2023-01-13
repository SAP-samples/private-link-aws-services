# AWS SQS PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-privatelink -c '{"serviceName": "com.amazonaws.eu-central-1.sqs"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create an SQS Queue

Create an SQS queue by following the [official documentation](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-create-queue.html).

Note the Queue URL, which looks similar to `https://sqs.us-east-1.amazonaws.com/123456789012/my-queue`.

## Create a user-provided service

Create a user-provided service to provide your AWS credentials as well as your SQS configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"queueUrl": "<queueUrl>", "accessKeyId": "<accessKeyId>", "secretAccessKey": "<secretAccessKey>", "region": "<awsRegion>"}'
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

## Query the endpoint to subscribe to messages

Put a message to the queue using the AWS console. Retrieve the automatically registered route of your application from the output of running `cf apps`.
Then, use curl to receive a message, the output of the application will show some logs.

```bash
curl 'https://<route>'

# View the Cloud Foundry application logs
cf logs sqs-pls-demo --recent
```
