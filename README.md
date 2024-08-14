# nisum-user-api
This is a project intended for applying to a position at Nisum. It was built with the following tech stack:
* Java SE 21
* Spring Boot 3.2.5
* Spring Data JPA
* Spring Security
* Gradle 8.7
* H2 (for integration tests)

## Local Deployment

### Prerequisites

* Java 21 or above
* Git
* Postman (or similar)

### Steps

1. Clone the following GitHub repository:
```
https://github.com/pipecm/nisum-user-api.gitt
```
2. Move to the project's directory:
```
cd nisum-user-api
```
3. Deploy locally the project using Docker Compose:
```
./gradlew clean build
```
4. After finishing the deployment, run the app and then, this cURL with Postman or other REST manager (No authentication required):
```
./gradlew bootRun
curl --location --request GET 'http://localhost:8080/actuator/health'
```
If the deployment is performed successfully, you should get a response similar to this:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 982818799616,
        "free": 818160590848,
        "threshold": 10485760,
        "path": "/home/pipecm/Development/nisum-user-api/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```
## Database info
You can access to the database using the H2 console:
```
http://localhost:8080/h2-console
```
## Users
### User creation
For creating users, you have to execute a cURL like this:
```
curl --location --request POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "John Jackson",
    "email": "john.jackson@nisum.com",
    "password": "fd07s9fd76D&",
    "phones" : [
        {
            "number": "95804761",
            "citycode": "9",
            "countrycode": "56"
        }
    ]
}'
```

| Parameter   | Description                      | Mandatory | Default value |
|-------------|----------------------------------|-----------|---------------|
| name        | First and last name of the user  | true      | N/A           |
| email       | Email of the user                | true      | N/A           |
| password    | Password of the user             | true      | N/A           |
| phones      | List of phones of the user       | true      | N/A           |
| number      | Phone number of the user         | true      | N/A           |
| citycode    | City code of the phone number    | true      | N/A           |
| countrycode | Country code of the phone number | true      | N/A           |

If the execution returns a `201 Created` status, the user was created successfully.

#### Sample response
```json
{
  "id": "43de1ca3-12b3-4f90-af23-b86059459430",
  "name": "John Jackson",
  "email": "john.jackson@nisum.com",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmphY2tzb25AbmlzdW0uY29tIiwiZW1haWwiOiJqb2huLmphY2tzb25AbmlzdW0uY29tIiwicm9sZXMiOltdLCJleHAiOjE3MjM2NzM1MzF9.CT5p0c6298lSJaefW9HV5XKoop3OdPPt5OXl2PJasQ4",
  "phones": [
    {
      "countrycode": "56",
      "citycode": "9",
      "number": "95804761"
    }
  ],
  "created": "2024-08-14T17:57:11.244603196",
  "modified": "2024-08-14T17:57:11.244603196",
  "last_login": "2024-08-14T17:57:11.69046207",
  "isactive": true
}
```

Also, you could verify the user creation in the database, running the following query:
```
SELECT * FROM api_user WHERE email = '{email}'
```

## Authentication
After creating a user, you can perform a login in the API using the following cURL:
```
curl --location --request POST 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "john.jackson@nisum.com",
    "password" : "fd07s9fd76D&"
}'
```
| Parameter | Description                                 | Mandatory | Default value |
|-----------|---------------------------------------------|-----------|---------------|
| username  | Email of the user who wants to authenticate | true      | N/A           |
| password  | Password registered for the user            | true      | N/A           |
#### Sample response
If login is successful, you should get a response like this:
```json
{
  "username": "john.jackson@nisum.com",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmphY2tzb25AbmlzdW0uY29tIiwiZW1haWwiOiJqb2huLmphY2tzb25AbmlzdW0uY29tIiwicm9sZXMiOltdLCJleHAiOjE3MjM2NzM5NDN9.Mu_nvuyGJ0INzBTTDS3GXdVfuNPFa6xvtpWdquFOAsk",
  "timestamp": "2024-08-14T18:04:03.921902943",
  "expiration": "2024-08-14T18:19:03"
}
```
The token validity (in minutes) can be set by modifying the value of the property `spring.security.jwt.expiration` in the file `src/main/resources/application.yml`.

## Swagger
You can find the Swagger documentation in the following location
```
http://localhost:8080/swagger-ui/index.html
```