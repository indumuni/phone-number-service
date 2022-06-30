# phone-number-service

### Framework/libraries used
- Java 11 
- SpringBoot 2.7.1 
- JUnit 5 
- Hamcrest 
- Gradle

### Design decisions 
- Try to keep thing simple as possible
- More emphasis on API/ Rest interface layer
- Followed TDD as where ever possible
- Not all edge cases are covered to deliver this in timely manner

### Notes:
- id and phoneNumberId represent the same and used inconsistently
- DTOs used across different layers since it is simple api
- Data are load from a flat file and not much emphasis given to repository implementation
- Service is barely empty, but keep that to separate controller and repository
- Not muh emphasis given to DTOs equal(), hashCode() and Serializable
- Logging can be improved

### How to run:
Run spring boot service

`./gradlew bootRun`

Run tests

`./gradlew test`

### Quick API reference 
Search API:

'GET: http://localhost:8080/PhoneNumbers?customerId=da04db53-e4fb-4c56-aa23-e03ddd7820dc&skip=1&limit=1'

Change status API

'PUT: http://localhost:8080/PhoneNumbers/26abe914-628b-4744-ad7c-430503c1ed2c/status'

'Body: {"active":true}'

Additional data can be found [here](./src/main/resources/FlatFilePhoneNumberRepository.txt)


Test coverage available [here](./build/reports/tests/test/index.html) after the test run

<img src="docs/test-coverage.png">


API specification available [here](./docs/phone-number-api-1.0.0-specifications.yaml)


