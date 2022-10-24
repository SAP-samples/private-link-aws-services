# AWS S3 PrivateLink Sample App

Private links to S3 offer many different interaction scenarios:
- Bucket & object access (via `bucket.<endpoint>` hostname)
- Bucket & object access using an access point (via `accesspoint.<endpoint>` hostname)
- Control access (via `control.<endpoint>` hostname)

See also <https://docs.aws.amazon.com/AmazonS3/latest/userguide/privatelink-interface-endpoints.html#privatelink-aws-sdk-examples>.

This sample application demonstrates all those different interactions.
In order to run the sample application with all of the above parts, please execute all of the following steps.

If you only want to test certain parts (i.e. only the bucket access, but don't care about the access point interaction), you can skip the necessary steps.

## Create an S3 VPC Endpoint

Create a VPC Endpoint to the service `com.amazonaws.us-east-1.s3` with type `interface`.

If using the SAP Private Link service, create a service instance using the following command:
```bash 
# adapt the region in the service name if using a different region
cf create-service privatelink standard my-service-instance-name -c '{"serviceName": "com.amazonaws.eu-central-1.s3"}'
```

To obtain the hostname, you can either create a service key or bind your app to the service instance.

## Create an S3 Bucket

- Create an S3 bucket
  using [the AWS documentation - Creating a bucket](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html)
- Upload an empty `index.html` file to the bucket.
- Create an access point from `Amazon S3 -> Access Points` section (not needed if you don't want to test the access point access)

## Run the application

The application requires some environment variables from the previous steps. 
Make sure that the application can access TCP port 443 on your VPC Endpoint.

- `S3_ENDPOINT_HOSTNAME` should look similar to `vpce-00000000000000000-00000000.s3.us-east-1.vpce.amazonaws.com`
- `S3_ACCESS_POINT_ARN` should look similar to `arn:aws:s3:us-east-1:123456789012:accesspoint:my-accesspoint-name`

````bash
S3_ACCOUNT_ID="<aws account id>" \
S3_ENDPOINT_HOSTNAME="<vpc endpoint DNS name>" \
S3_ACCESS_POINT_ARN="<Access Point ARN or dummy if you don't need it>" \
AWS_ACCESS_KEY_ID="<username>" \
AWS_SECRET_ACCESS_KEY="<password>" \
./mvnw spring-boot:run
````

## Query the endpoint to interact with the S3 endpoint

Use curl to send requests to interact with the service. The output of the application will show some logs.

- `/bucket/` uses the endpoint URL for `S3 bucket API` `https://bucket.<custom_endpoint>`
- `/control/` uses the endpoint URL for `S3 control API` `https://control.<custom_endpoint>`  
- `/accesspoint/` uses the endpoint URL for `S3 access point API` `https://accesspoint.<custom_endpoint>`

```bash
BUCKET_NAME="<Bucket name from step `Create an S3 Bucket`>"
FILE_NAME="index.html" # an available object in the selected S3 bucket.
curl "127.0.0.1:8080/buckets" # Listing all available buckets
curl "127.0.0.1:8080/bucket/${BUCKET_NAME}" # Send a request to list objects of a specific bucket
curl "127.0.0.1:8080/bucket/${BUCKET_NAME}/${FILE_NAME}" # Send a request to get an object from the defined bucket
curl "127.0.0.1:8080/accesspoint/${FILE_NAME}" # Send a request to get an object from the access point
curl "127.0.0.1:8080/control/list-ap/${BUCKET_NAME}" # Send a request to list access points from the defined bucket
```
