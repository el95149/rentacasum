# Introduction

Welcome to Rent-A-Casum, a simpleton Movie store.

Key features include:

* Database featuring Customers, Users/employees, Movies and Rentals.
* HTTP REST-API end-points.
*  Configurable prices rates. 

# Design & Implementation decisions

## DropWizard
* First-time ever use of DropWizard (v.1.1.0). Personal preference would have been Spring Data Rest, but I wanted to demonstrate quick adoption of an unknown framework, as well as compare it to SDR.  
* Based application on the 'dropwizard-example' project, refactored and custom-tailored to suit needs.

## Project structure

The project consists of the following Maven modules:

* rentacasum: parent module.  
    * rentacasum-domain: JPA/Hibernate based domain entities.
    * rentacasum-api: Representations/POJOs used for REST requests/responses.
    * rentacasum-service: Hibernate DAOs and business logic service classes.
    * rentacasum-application: DropWizard HTTP REST app (Resources, config, security etc.)
 *  Hierarchy-wise, 'application' sits on top of 'service', which sits on top of 'api' which, finally, sits on top of 'model'
 * Main purpose for using multiple modules was separation of concerns.

## Database

The following entities are defined (also see adjacent rentacasum_schema.png):

* Customer
* Movie
* Rental
* User

* These  are, pretty much, self-explanatory. Relationship-wise, only the Rental entity is connected many-to-one to both the Customer and the Movie entities, acting as an 'rich' relationship between them.
* Each time a customer rents a movie, a rental row is created, containing all charges (initial and extra), as well as renting/return date information and bonus points.
* Total bonus points for each customers are stored in the customer entity (for easy access), but can also be calculated from the customer's rentals.

## Hibernate/JPA

* Hibernate/JPA 2.x: Model resides in module 'rentacasum-domain'. Can act as standalone JPA model, since no DropWizard specific dependencies have been included.
* All entites extend a BaseEntity (i.e. via @MappedSuperclass), allowing for easy common field management.
* DAOs can be found under module 'rentacasum-service'.

## Database Migrations

* Migrations.xml resides under module 'rentacasum-domain'. Liquibase Maven plugin was used generation.
* If future model changes are required, it's as easy as changing the POJOs and running 'mvn liquibase:diff' inside 'rentacasum-domain' (and of course, using the 'db migrate' command on the application itself).
* Database comes pre-loaded with sample Customer, User and Movie data, loaded from 'rentacasum-domain/src/main/resources/init_data.sql' when migrating the db. 
 
## Security

* Security is in place, as per the dropwizard-example paradigm, but not used in this application. It is assumed that all operations are performed by a store employee, who already has a right to do pretty much anything.
* A User entity/table has been put in place, containing a single, sample, employee. It's not used in any context right now, but has been put there mainly for future use.

## General Notes
* No views or static HTML assets have been used, the entire applicaiton is a REST API, consuming and serving JSON data.

# Running The Application

To test the example application run the following commands.

* To package the example run the following from the root rentacasum directory.

		mvn package
        
* Change into the application directory.

		cd rentacasum-application

* To setup the h2 database run.

		java -jar target/rentacasum-application-0.0.1-SNAPSHOT.jar db migrate rentacasum.yml

* To run the server run.

		java -jar target/rentacasum-application-0.0.1-SNAPSHOT.jar server rentacasum.yml

# Application REST End-points

* All following POST requests assume a content type of 'application/json', unless otherwise specified. All GET requests return 'application/json'.
* Response statuses for all requests are set, in general, as follows:  
    * 200: All went ok.
    * 400: Something wasn't correct in the request  sent.
    * 409: Tried to perform an illegal operation (such as re-renting an already rented movie).
    * 500: Something went seriously wrong (i.e. database is corrupt).
	 

##  Get a single customer

	GET /customers/{customerId}
	
## Search customers by term

	GET /customers?term=<textual term>&start=<row offset>&rows=<rows to fetch>
	
Searching is performed in various customer fields.
* 'term' is required, but can be left empty, for a full-field search.
* 'start' and 'rows' default to 0 and 10, respectively, if left empty.
	
## Search movie inventory by term

	GET /movies?term=<textual term>&onlyAvailable=<true|false>&start=<row offset>&rows=<rows to fetch>

* Searching is performed in various customer fields.
* 'term' is required, but can be left empty, for a full-field search.
* 'onlyAvailable', when set to 'true', fetches only movies NOT rented (defaults to 'false').
* 'start' and 'rows' default to 0 and 10, respectively, if left empty.

## Rent movies 

	POST /rentals

	Sample Request body:
	{
	    "customerId": 1,
	    "movies": [
	        {
	            "movie": 1,
	            "days": 2
	        },
	        {
	            "movie": 3,
	            "days": 3
	        },
	        {
	            "movie": 5,
	            "days": 4
	        }
	    ]
	}
	
* Response includes initial charges, along with bonues points earned.
	
## View active (i.e. not returned) rentals for customer
	
	GET /rentals?customerId=<customerId>

* 'customerId' is required.
* Response includes a  list of not-returned rentals	
	
## Return movies

	POST /rentals/return
	
*NOTE*: "date" is optional, aimed mostly for testing purposes.
	If not present, "today's" date is used by default.

	Sample Request body:
	{
    "customerId": 1,
    "movies": [1,2],
    "date": "2017-05-02T00:00:00.000+0000"
	}

* Response includes any extra charges applied.