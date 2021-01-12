# delunico-library https://delunico-library.herokuapp.com/
This ebooks web application is designed to display information about all different kinds of ebooks in a dynamic, responsive, and intuitive interface. It implements technologies such as the spring boot web framework, spring security, spring mail, thymeleaf templates, crud, lombok, heroku, and postgresql heroku db add on.
  
# Security
#### Password Recovery: ####
Your password can be recovered at <a href = "https://delunico-library.herokuapp.com/recover-account">recover here</a> After your email is confirmed, this application will automatically email you a new generated password to recover your account. Then, you may go to your account settings and reset your password if desired.
#### Authorization and Authentication: ####
 Only authorized manager role users can access and view the add a book link located in the nav bar.<br>
 Only Users can write reviews and access MyBooks and Account pages
 
 # Technologies Incorporated
<ul>
  <li>Spring Boot Framework</li>
  <li>Spring Security Framework</li>
  <li>Spring Mail</li>
  <li>Thymeleaf</li>
  <li>Lombok</li>
  <li>Postgresql</li>
  <li>Crud</li>
</ul>

## Hosting ##
This project was hosted on the free heroku web server. All commits and updates to this project were performed using the heroku CLI and bash interface.

## Database ##
The postgresql database is achieved by integrating the heroku postgres add on with my spring app. 

## Design Pattern ##
<ul>
  <li>MVC design pattern</li>
  <li>JdbcTemplate</li>
  <li>Dependency Injection </li>
</ul>
 
# Features
#### MyBooks: ####

  The MyBooks page will show an account's favourited books all in one convenient location. Books can be removed and or added from the mybooks page when necessary.
#### Browse: ####

  The Browse page will show my small collection of 12 books I added to my heroku postgresql database as an example. 
#### Search: ####

  The search bar located within the nav bar can search for any book in the database when even given just a partial title. This is achieved with SQL's like operator.
#### Reviews: ####

  Each book has a view page where you can read a book description, some posted reviews and see its average star rating as well.
#### Add Books: ####

  On this page a manager can input a book title, author, description, and an optional picture as well. If no picture is provided, a default image will take its place. 
#### Responsiveness: ####

  This application is responsive to almost all viewports by using css @media selectors and more. It's content will move depending on the device being mobile, tablet, or computer. It's nav bar will also transform into a hamburger menu with a cleaner more mobile friendly look at smaller viewports. 

# Challenges #

## Spring security and heroku ##
Spring security by default creates their schema for accounts in a table named users. In postgresql the word users is reserved as a keyword and will not allow it to be used for a table name. To fix this issue I had to learn how to integrate the spring security framework with my own custom schema with custom tables such as user_table.  

