# Library-Managemet-System-DBMS-Project
A Library Management System made using mySQL and Java Swing.

# Setting-Up:
To run this project, please make sure to download JDBC connector, and linking it with your respective IDE (intellij preferably).
Once you have linked it to your IDE, follow the following steps:
1) Download the entire project.
2) Run the dbfinalcreate.sql and procfinal.sql on mySQL workbench to create the database for the project.
3) Open the java project in your respective IDE (main files for frontend are included in DBS Project-Group 45 > src > bms).
4) Open the Conn file in the IDE and change the "root" (connection name) and "123456" (Password) in the line
" c = DriverManager.getConnection("jdbc:mysql:///library_db1", "root", "123456"); " to your mySQL workbench connection name
and password.
5) Run the project by running the login page of the project.

