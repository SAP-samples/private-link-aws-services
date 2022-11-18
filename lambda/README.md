# AWS Lambda PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Lambda VPC Endpoint
Create a VPC Endpoint to the service `lambda` eg. `com.amazonaws.us-east-1.lambda`.
Note the hostname, it should look similar to `vpce-00000000000000000-00000000.lambda.us-east-1.vpce.amazonaws.com`.

If using the SAP Private Link service, create a service instance using the following command - this will create the interface endpoint for you:
```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-service-instance-name -c '{"serviceName": "com.amazonaws.us-east-1.lambda"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create a Lambda function
[Create a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/getting-started.html#getting-started-create-function) and [deploy the function](https://docs.aws.amazon.com/lambda/latest/dg/configuration-function-zip.html#configuration-function-update) in [`example/helloWorld.zip`](example/helloWorld.zip).

## Run the application

```bash
LAMBDA_ENDPOINT_URL="<url>" \
LAMBDA_FUNCTION_NAME="<function-name>" \
AWS_ACCESS_KEY_ID="<access_key_id>" \
AWS_SECRET_ACCESS_KEY="<secret_access_key>" \
./mvnw spring-boot:run
```

## Query the endpoint to invoke the Lambda function

```bash
curl '127.0.0.1:8080'
```