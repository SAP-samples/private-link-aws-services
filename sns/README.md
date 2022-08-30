# AWS SNS PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create an SNS VPC Endpoint
Create a VPC Endpoint to the service `sns` eg. `com.amazonaws.us-east-1.sns`.
Note the hostname, it should look similar to `vpce-00000000000000000-00000000.sns.us-east-1.vpce.amazonaws.com`.

## Create an SNS Topic
Create an SNS topic by following the [official documentation](https://docs.aws.amazon.com/sns/latest/dg/sns-create-topic.html).

Note the Topic ARN, which looks similar to `arn:aws:sns:us-east-1:123456789012:mytopic`.

## Run the application

```bash
SNS_ENDPOINT_URL="<url>" \
SNS_TOPIC_ARN="<topic-arn>" \
AWS_ACCESS_KEY_ID="<access_key_id>" \
AWS_SECRET_ACCESS_KEY="<secret_access_key>" \
./mvnw spring-boot:run
```

## Query the endpoint to publish a message
Use curl to send a message, the output of the application will show some logs
```bash
curl '127.0.0.1:8080?message=testmsg'
```