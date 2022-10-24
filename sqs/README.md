# AWS SQS PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create an SQS VPC Endpoint
Create a VPC Endpoint to the service `sqs` eg. `com.amazonaws.us-east-1.sqs`.
Note the hostname, it should look similar to `vpce-00000000000000000-00000000.sqs.us-east-1.vpce.amazonaws.com`.

If using the SAP Private Link service, create a service instance using the following command - this will create the interface endpoint for you:
```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink standard my-service-instance-name -c '{"serviceName": "com.amazonaws.eu-central-1.sns"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create an SQS Queue
Create an SQS queue by following the [official documentation](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-create-queue.html).

Note the Queue URL, which looks similar to `https://sqs.us-east-1.amazonaws.com/123456789012/my-queue`.

## Run the application

```bash
SQS_ENDPOINT_URL="<url>" \
SQS_QUEUE_URL="<queue-url>" \
AWS_ACCESS_KEY_ID="<access_key_id>" \
AWS_SECRET_ACCESS_KEY="<secret_access_key>" \
./mvnw spring-boot:run
```

## Query the endpoint to subscribe to messages
Put a message to the queue using the AWS console.

Then, use curl to receive a message, the output of the application will show some logs
```bash
curl '127.0.0.1:8080'
```