Setup and Configuration:

1.	Database Configuration: Users must create a MySQL database and update the application.properties file accordingly to ensure the application connects to the correct database instance. Current application.properties is set to database name – “FinanceFlow”. So, user can create MySQL database called FinanceFlow to get application running correctly.

2.	Credentials Management: Update the username and password in the application.properties file to match the database credentials. Current settings use “root” as a username and “password” as a password.

3.	Running the Application: User must direct to the file – “FinanceFlow” and then the application can be launched using the Maven command in command prompt – “mvn spring-boot:run”. This command compiles the application and starts a web server.

4.	Accessing the Application: Once the application is running, the application is accessible through web browser at http://localhost:9191.