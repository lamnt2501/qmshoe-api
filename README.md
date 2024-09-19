# QMShoe API

QMShoe API is a api producer web application

## Installation

Clone repository to your local machine

```bash
git clone https://github.com/lamnt2501/qmshoe-api.git
```

## How to run
- Create an .env file in root folder and add 4 line below

```
.env file
CLOUDINARY_URL=cloudinary://<your-key>:<your-secret>@<your-username> // for cloudinary upload service
JWT_SECRET=<your-secret> // for security with jwt
EMAIL_USERNAME=<your-email> // for send mail
EMAIL_PASSWORD=<your-email-password>
```
- Go to Resource folder and change these lines in application-dev.yml
```
    username: <your-database-account-name>
    password: <password-for-this-account>
```
- Ok now, open terminal and run!
```
mvn clean install spring-boot:run
```
