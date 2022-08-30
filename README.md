# Sample apps to connect to AWS services over private link

This repository contains sample applications that demonstrate how the AWS SDK has to be configured so that the traffic goes over Private Link.

Be aware that the sample apps read the necessary configuration (endpoint hostnames, credentials and further values) from environment variables.
In a productive scenario, you want to read the configuration from either the destination service, the SAP Private link service instance bindings or user-provided-services (for the AWS credentials).

## Requirements
The apps are based on Spring Boot and require a recent JDK installation (>= Java 17).

## How to obtain support
[Create an issue](https://github.com/SAP-samples/private-link-aws-services/issues) in this repository if you find a bug or have questions about the content.
 
For additional support, [ask a question in SAP Community](https://answers.sap.com/questions/ask.html).

## Contributing
If you wish to contribute code, offer fixes or improvements, please send a pull request. Due to legal reasons, contributors will be asked to accept a DCO when they create the first pull request to this project. This happens in an automated fashion during the submission process. SAP uses [the standard DCO text of the Linux Foundation](https://developercertificate.org/).

## Code of Conduct
Refer to [CODE OF CONDUCT](https://github.com/SAP-samples/.github/blob/main/CODE_OF_CONDUCT.md) file.

## License
Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSE) file.
