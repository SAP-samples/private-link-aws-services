# AWS SNS PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink standard my-privatelink -c '{"serviceName": "com.amazonaws.eu-central-1.sns"}'
```

## Create an SNS Topic
Create an SNS topic by following the [official documentation](https://docs.aws.amazon.com/sns/latest/dg/sns-create-topic.html).

Note the Topic ARN, which looks similar to `arn:aws:sns:us-east-1:123456789012:mytopic`.

## Create a user-provided service

Create a user-provided service to provide your AWS credentials as well as your SNS configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"topicArn": "<topicArn>", "accessKeyId": "<accessKeyId>", "secretAccessKey":"<secretAccessKey>", "region": "<awsRegion>"}'
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

## Query the endpoint to publish a message

Retrieve the automatically registered route of your application from the output of running `cf apps`.
Use curl to send a message, the output of the application will show some logs. 

```bash
curl 'https://<route>?message=testmsg'

# View the Cloud Foundry application logs
cf logs sns-pls-demo --recent
```