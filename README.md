# https://elibrary.up.railway.app/

This ebooks web application is designed to display information about all different kinds of ebooks in a dynamic, responsive, and intuitive interface. It implements technologies such as the spring boot web framework, spring security, spring mail, thymeleaf templates, crud, lombok, heroku, and postgresql heroku db add on.

## Technologies Incorporated
<ul>
  <li>Spring Boot Framework</li>
  <li>Thymeleaf templating markup language</li>
  <li>PostgreSQL</li>
  <li>Spring Mail Dependency</li>
  <li>Lombok Dependency</li>
</ul>

## Security
<ul>
  <li><b>Spring Security:</b> implements Spring Security Dependency for authorizing page access, securing database, and creating user accounts. </li>
  <li><b>Authorization:</b> Uses Ant Matchers to check if a user has enough privileges to access a path url to a specific web page.</li>
  <li><b>Authentication:</b> Authenticates a user from their username or email and password stored in the database</li>
  <li><b>Encryption:</b> All passwords are encrypted using a hash algorithm from BCrypt and are stored as a hash in the database</li>
  <li><b>Password Recovery:</b> a user can request the app to email them a new password to regain access to their account, afterwhich they may change it to their liking</li>
  <li><b>Protection: </b>User's data is protected by incorporating HTTPS SSL certificates as well as restricting access in searchbar queries to block SQL injection attacks </li>
</ul>

## Languages
<ul>
  <li>Java</li>
  <li>JavaScript</li>
  <li>HTML + CSS</li>
</ul>

## Hosting
The project Spring Boot app was both hosted on railway.app configured with a CICD automatic deploys connected to this repository's main branch.

## Database
This postgreSQL database was also deployed by railway.app and integrated with the spring app. Passwords are hash encrypted. Data is normalized to level 3. 

## Design Patterns
<ul>
  <li>MVC design pattern</li>
  <li>Dependency Injection</li>
  <li>Autowired Singletons</li>
</ul>

## Features

#### CRUD: ####
<ul>
  <li>Each Book's information and details can be added, edited, or deleted as desired if the authenticated user has admin privileges. </li>
  <li>Authenticated users may insert new reviews for a book.</li>
  <li>Authenticated users may favourite books and is saved in the database for when they return.</li>
  <li>Authenticated users may update their account information as required </li>
</ul>

#### Reviews ####

 Each book has a list of reviews saved in the database. It will take the average star rating and display that to the user on the viewBook page followed by the reviews themselves.
  
#### Navbar: ####
 
 The navbar contains different options depending on the privileges of the user. If they are an admin, it displays an add book link. If a user tries to click a link or url they don't have sufficient privileges for, they will be redirected to a permission denied page and prompted to authenticate.
 
#### Responsiveness: ####

  This application is responsive to almost all viewports by using css @media selectors and more. The font-size, margins, and layout will change depending on the viewport dimensions of the device such as mobile, tablet, or desktop. 
  

