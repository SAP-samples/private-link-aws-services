# AWS S3 PrivateLink Sample App

The Private Link service supports several scenarios for S3:
- Bucket & object access (via `bucket.<endpoint>` hostname)
- Bucket & object access using an access point (via `accesspoint.<endpoint>` hostname)
- Control access (via `control.<endpoint>` hostname)

See also <https://docs.aws.amazon.com/AmazonS3/latest/userguide/privatelink-interface-endpoints.html#privatelink-aws-sdk-examples>.

This sample application demonstrates these scenarios.
In order to run the sample application with all of the above parts, please execute following steps.

If you only want to test certain parts (i.e. only the bucket access, but don't care about the access point interaction), you can skip the necessary steps.

## Create a Private Link service instance

Create a Private Link service instance by running the following command:

```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink beta my-privatelink -c '{"serviceName": "com.amazonaws.eu-central-1.s3"}'
```

## Create an S3 Bucket

- Create an S3 bucket
  using [the AWS documentation - Creating a bucket](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html)
- Upload an empty `index.html` file to the bucket.
- Create an access point from `Amazon S3 -> Access Points` section (not needed if you don't want to test the access point access)

## Create a user-provided service

Create a user-provided service to provide your AWS credentials as well as your S3 configuration:

```bash 
# adapt the properties according to your setup
cf cups my-service-config -p '{"accountId": "<accountId>", "accessPointArn": "<accessPointArn>", accessKeyId": "<accessKeyId>", "secretAccessKey":"<secretAccessKey>", "region": "<awsRegion>"}'
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

## Query the endpoint to interact with the S3 endpoint

Retrieve the automatically registered route of your application from the output of running `cf apps`.
Use curl to send requests to interact with the service. The output of the application will show some logs.

- `/bucket/` uses the endpoint URL for `S3 bucket API` `https://bucket.<custom_endpoint>`
- `/control/` uses the endpoint URL for `S3 control API` `https://control.<custom_endpoint>`  
- `/accesspoint/` uses the endpoint URL for `S3 access point API` `https://accesspoint.<custom_endpoint>`

```bash
BUCKET_NAME="<Bucket name from step `Create an S3 Bucket`>"
FILE_NAME="index.html" # an available object in the selected S3 bucket.
curl "https://<route>/buckets" # Listing all available buckets
curl "https://<route>/bucket/${BUCKET_NAME}" # Send a request to list objects of a specific bucket
curl "https://<route>/bucket/${BUCKET_NAME}/${FILE_NAME}" # Send a request to get an object from the defined bucket
curl "https://<route>/accesspoint/${FILE_NAME}" # Send a request to get an object from the access point
curl "https://<route>/control/list-ap/${BUCKET_NAME}" # Send a request to list access points from the defined bucket

# View the Cloud Foundry application logs
cf logs s3-pls-demo --recent
```
