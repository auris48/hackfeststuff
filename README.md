Coverage: 63%
# IMS-Starter

A CLI CRUD application developed in Java that uses a MYSQL database to manage orders, customers and items.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development
and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You'll need - Java version 8 and a command line interface to run the file.

For development, you'll need to install Maven 3.8.6 and have a compatible IDE.

### Installing

To get the development environment running, clone the repository from GitHub. And in your IDE, import the project
as a maven project. This should be detected automatically anyway, if not, right click .pom file and 
generate/download sources.

Afterwards run mvn package and again update folders/sources.

## Running the tests

To run the tests, type in mvn test in your IDE's Git Terminal, you might have to enable that.
Alternatively, right click on java folder and run JUNIT tests that way or each file in test folder.

### Unit Tests 

The unit tests mainly test that the object that is retrieved from the database will match the expected object. 
When certain functions are performed on it.

They mainly test that the classes and SQL database work together in unison and data integrity is preserved.

They also test whether database functions work, so if I want to delete an item for example
it should check the database, see that it contains the item and then return the id of that item to signify
the item was deleted. If I try to read an item with that ID again I should not be able to. 

They also test how you're comparing objects, you need to make sure that two objects with the same values
are equal for example.

For that JUNIT tests classes that deal with database directly. 

I've used JUNIT 5 for my unit tests.  

### Integration Tests 

The integration tests check whether classes/methods work in the expected manner. This would typically need to be
done on the controller classes as they manage integration between SQL and the data. 

I've used Mockito to generate Mock methods and modelled expected behavior and output.

For example when user enters details to create an object, this object should be returned from another class. That
would deal with the database itself

## Deployment

To deploy this project simply execute the jar with dependencies file from the Fat Jar folder in repositories.

Open CMD, type in java -jar ims-0.0.1-jar-with-dependencies.jar And this should run the system.

To deploy the project from a development environment, go to Terminal and type in mvn install. 

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [Git] for versioning.

## Authors

* **Aurimas Pilnikovas** - *Initial work* - [aurimaspilnikovas](https://github.com/auris48)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

*For help in [Choosing a license](https://choosealicense.com/)*

## Acknowledgments

StackOverflow and all colleagues/trainers at QA. 
