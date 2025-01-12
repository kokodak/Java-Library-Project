# Java-Library-Project
This project is a simple library management system using Java GUI and MySQL for Java programming class.

## Getting Started

This system uses MySQL in a Docker container environment.

To run the system properly, execute the command below in the root directory of the project:

```bash
docker-compose up -d
```

**(Note)** If you launch the GUI program too quickly after starting the container, it may attempt to communicate with the MySQL container before it is fully ready, which could result in an error. Therefore, please wait a moment after starting the container before running the GUI program.

https://docs.google.com/presentation/d/1P6CfXkD8XU9rK7mnTMwlhzzSpubEdl9_2-6_RU7dseE/edit?usp=sharing
