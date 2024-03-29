[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/private-link-aws-services)](https://api.reuse.software/info/github.com/SAP-samples/private-link-aws-services)
# Sample apps to connect to AWS services using SAP Private Link service

This repository contains sample applications for a selected list of AWS services, demonstrating how the AWS SDK must be configured so that the traffic goes over SAP Private Link service.

The sample applications are specifically developed for the use of the SAP Private Link service provided on CloudFoundry. Each sample application directory
contains a dedicated README with instructions on how to run the application.

For a full list of currently supported AWS services, please visit our [official documentation](https://help.sap.com/docs/PRIVATE_LINK/42acd88cb4134ba2a7d3e0e62c9fe6cf/575341947b854a82a9f3ba2bc6b1b6cc.html?locale=en-US#supported-services).

## Requirements
The apps are based on Spring Boot and require a recent JDK installation (>= Java 17).

## How to run

```bash
# Clone the repository
git clone https://github.com/SAP-samples/private-link-aws-services

# switch to a sample app, e.g. s3
cd private-link-aws-services/s3

# then, refer to the README of the subdirectory how to run it.
cat README.md
```

## How to obtain support
[Create an issue](https://github.com/SAP-samples/private-link-aws-services/issues) in this repository if you find a bug or have questions about the content.
 
For additional support, [ask a question in SAP Community](https://answers.sap.com/questions/ask.html).

## Contributing
If you wish to contribute code, offer fixes or improvements, please send a pull request. Due to legal reasons, contributors will be asked to accept a DCO when they create the first pull request to this project. This happens in an automated fashion during the submission process. SAP uses [the standard DCO text of the Linux Foundation](https://developercertificate.org/).

## Code of Conduct
Refer to [CODE OF CONDUCT](https://github.com/SAP-samples/.github/blob/main/CODE_OF_CONDUCT.md) file.

## License
Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSE) file.
