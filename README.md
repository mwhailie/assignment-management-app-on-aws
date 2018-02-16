# CSYE6225

This is a web application based on Spring Boot, RESTful API and deployed on AWS.

## Team Members

* **Shirui Wang** , *001226459* , wang.shirui@husky.neu.edu
* **Wenhe Ma** , *001238705* , ma.wenhe@husky.neu.edu
* **Yuting Jing** , *001221590* , jing.yu@husky.neu.edu
* **Haoan Yan** , *001220895* , yan.hao@husky.neu.edu

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The web framework used
* [Gradle](https://gradle.org/) - Dependency Management
* [IntelliJ](https://www.jetbrains.com/idea/) - IDE used to develop the web app

## Set up

To run this project on local environment, you may have to change the following code in application.properties file.
```
spring.datasource.url=jdbc:mysql://localhost:3306/db_csye6225
spring.datasource.username=root
spring.datasource.password=root
```
as your database setting

Then execute the following command in your mysql
```
create database db_csye6225;
```

## Deployment

* Deployed with [TravisCI](https://travis-ci.com) and [AWS CodeDeploy](https://aws.amazon.com/codedeploy/).

## REST API Endpoints

| HTTP method  | URI path | Description |
| ------------- | ------------- |  ------------- |
| GET  | /  | Get the home page |
| POST  | /user/register  | Create account for user |
| GET  | /tasks  | Get all tasks for the user |
| POST  | /tasks  | Create a to-do task for the user |
| PUT  | /tasks/{id} | Update a to-do task for the user |
| DELETE  | /tasks/{id} | Delete a to-do task for the user |
| GET  | /tasks/{id}/attachments  | Get list of files attached to the to-do task |
| POST  | /tasks/{id}/attachments  | Attach a file to the to-do task |
| DELETE  | /tasks/{id}/attachments/{idAttachments} | Delete file attached to the to-do task |
| POST  | /forget-password | Password reset |

## Make Unauthenticated HTTP Request

Execute following command on your bash shell
``` 
$ curl https://csye6225-fall2017-mawenhe.me/csye6225app
```

### Expected Response:
```
{"message":"you are not logged in!!!"}
```

## Authenticate for HTTP Request

Execute following command on your bash shell
```
$ curl -X POST \
  http://csye6225-fall2017-mawenhe.me/csye6225app \
  -H 'authorization: Basic MUAxOjE=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 9362bd7c-8dc6-22b2-6e9f-60696512376e'
```
where *authorization* is given to a registered user with username and password.

### Expected Response:
 ```
 {"message":"you are logged in. current time is Tue Sep 19 20:03:49 EDT 2017"}
 ```

## GitHub Releases
* [v8.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v8.0) - Assignment 10
  - Load Balancer routing request to 3 instance
  - SSL certificate
* [v7.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v7.0) - Assignment 9
  - CloudWatch, logs
* [v6.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v6.0) - Assignment 8
  - Pulished SNS topic to trigger Lambda function
  - Generated Tolken that stored in DynamoDB with 20 mins TTL
  - Used SES to send password reset tolken via email
* [v5.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v5.0) - Assignment 7
  - Deployed the web app on instance
* [v4.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v4.0) - Assignment 6
  - Google Cloud
* [v3.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v3.0) - Assignment 5
* [v2.1](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v2.1) - Assignment 4
  - Implemented web app.
  - CloudFormation template
* [v1.0](https://github.com/mwhailie/csye6225-fall2017/releases/tag/v1.0) - Assignment 3
  - Spring Boot Starter app

## TravisCI

* [TravisCI](https://travis-ci.com/mwhailie/csye6225-fall2017/builds/) - TravisCI builds
