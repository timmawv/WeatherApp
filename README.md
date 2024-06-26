# Weather App

## Description

`Weather App` - It is MVC Java Servlet App with Unit and Integration tests for testing functionality.
It has a view with thymeleaf pages you can check weather any city and create account for saving locations in DB.
It has login and registration system. Also, it has a login and registration validation it means you can't create an account with 
invalid fields or with empty fields, you can't create an easy password, and you can't create account with same login which 
was created before. This app has a session system that controls pages that user can visit. If you aren't logged you can't save locations.
and you can't visit pages weather and weather/*
As well I added liquibase for migrations, easy way to create tables and add some data. 
I added ci/cd for easy testing and pushing to docker hub my app.
This app has Dockerfile and docker compose too. 

<!-- Project from this [course](https://zhukovsd.github.io/java-backend-learning-course/) -->

## Project Stack

* Servlets
* Java EE
* Session Management
* Docker
* Docker compose
* CI/CD 
* GitHub Actions
* Postgres, H2
* Liquibase
* Slf4j
* Integration testing, JUnit 5, Mockito
* Thymeleaf, HTML, CSS, JS, Chart.JS
* Hibernate
* API Weather
* JSON

## Pages of project

### Logged Page

<img src="img/git/logged_page_location.png">

### Main page

<img src="img/git/main-page.png">

### Login page

<img src="img/git/login.png">

### Registration page

<img src="img/git/registration.png">

### Success registration

<img src="img/git/success_registration.png">

### Search weather full view

<img src="img/git/search.png">

### Search weather short view

<img src="img/git/search_short_view.png">

### Forecast hourly

<img src="img/git/forecast_hourly.png">

### Forecast weekly

<img src="img/git/forecast_weekly.png">
