# AWS SES PrivateLink Sample App

In order to run the sample application, please execute the following steps:

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-privatelink -c '{"serviceName": "com.amazonaws.eu-central-1.email-smtp"}'
```

**Note:** Using the API to send mails via PrivateLink is currently not supported.

## Create an SES identity

By default, new accounts are placed in the [SES sandbox](https://docs.aws.amazon.com/ses/latest/dg/request-production-access.html).
To actually run this sample app, you'll have to
[create & verify an SES identity](https://docs.aws.amazon.com/ses/latest/dg/creating-identities.html#verify-email-addresses-procedure).
Note the email address.

## Obtain SES SMTP Credentials

SES requires an SES-specific user and password, not the standard access key id and secret access key. See 
[the AWS documentation](https://docs.aws.amazon.com/ses/latest/dg/smtp-credentials.html) on how to obtain these 
credentials. Note the username and password.

## Create a user-provided service

Create a user-provided service to provide your SES configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"username": "<username>", "password": "<password>", "from": "<sender mail address>", "to": "<receiver mail address>"}'
```

## Build and push the application

Make sure that the application can access TCP port 2587 on your VPC Endpoint.

Build the application and push it to CloudFoundry:

```bash
./mvnw package
cf push
```

The `cf push` command will automatically bind the Private Link service instance and the user-provided service to the pushed application
as defined in the [manifest file](manifest.yml).

**Note: Be aware that the pushed application is publicly accessible via the provided route and should therefore be removed after testing.**

## Query the endpoint to send an e-mail

Retrieve the automatically registered route of your application from the output of running `cf apps`.
Use curl to send the e-mail, the output of the application will show some logs.

```bash
curl 'https://<route>?body=testmsg'

# View the Cloud Foundry application logs
cf logs ses-pls-demo --recent
```

