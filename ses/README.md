# AWS SES PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create an SES VPC Endpoint
Create a VPC Endpoint to the service `email-smtp` (using the API to send mails via PrivateLink is currently not supported),
eg. `com.amazonaws.us-east-1.email-smtp`.
Note the hostname, it should look similar to `vpce-00000000000000000-00000000.email-smtp.us-east-1.vpce.amazonaws.com`.

If using the SAP Private Link service, create a service instance using the following command - this will create the interface endpoint for you:
```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink standard my-service-instance-name -c '{"serviceName": "com.amazonaws.eu-central-1.email-smtp"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create an SES identity
By default, new accounts are placed in the [SES sandbox](https://docs.aws.amazon.com/ses/latest/dg/request-production-access.html).
To actually run this sample app, you'll have to
[create & verify an SES identity](https://docs.aws.amazon.com/ses/latest/dg/creating-identities.html#verify-email-addresses-procedure).
Note the email address.

## Obtain SES SMTP Credentials
SES requires an SES-specific user and password, not the standard access key id and secret access key. See 
[the AWS documentation](https://docs.aws.amazon.com/ses/latest/dg/smtp-credentials.html) on how to obtain these 
credentials. Note the username and password.

## Run the application
The application requires some environment variables from the previous steps. Make sure that the application can access
TCP port 2587 on your VPC Endpoint.
````bash
SPRING_MAIL_HOST="<vpc endpoint hostname>" \
SPRING_MAIL_USERNAME="<username>" \
SPRING_MAIL_PASSWORD="<password>" \
SES_SAMPLE_TO="<ses identity mail>" \
./mvnw spring-boot:run
````

## Query the endpoint to send an e-mail
Use curl to send the e-mail, the output of the application will show some logs
```bash
curl '127.0.0.1:8080?body=testmsg'
```

