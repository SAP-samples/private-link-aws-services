# AWS Lambda PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-privatelink -c '{"serviceName": "com.amazonaws.us-east-1.lambda"}'
```

## Create a Lambda function

[Create a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/getting-started.html#getting-started-create-function) and [deploy the function](https://docs.aws.amazon.com/lambda/latest/dg/configuration-function-zip.html#configuration-function-update) in [`example/helloWorld.zip`](example/helloWorld.zip).

## Create a user-provided service

Create a user-provided service to provide your AWS credentials as well as your Lambda configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"functionName": "<functionName>", "accessKeyId": "<accessKeyId>", "secretAccessKey": "<secretAccessKey>", "region": "<awsRegion>"}'
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

## Query the endpoint to invoke the Lambda function

Retrieve the automatically registered route of your application from the output of running `cf apps`.
Use curl to send a request to the sample application, which will trigger the lambda function.

```bash
curl 'https://<route>'

# View the Cloud Foundry application logs
cf logs lambda-pls-demo --recent
```