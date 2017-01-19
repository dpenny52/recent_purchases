Instructions to run the application:

1. Run redis-server locally.

This application uses Redis for caching, so to run the application locally you will need a Redis server running.

Use this quickstart guide: https://redis.io/topics/quickstart

Or install using Homebrew (for Mac):
brew install redis

Once you have Redis installed, run it with the command:
redis-server

The app is initially configured to use Redis's default hostname and port, but if you with to run using a non-standard setup you must set your hostname and port, and optionally the time for cached values to expire, in the following properties (src/main/resources/application.properties):
redis.hostname
redis.port
redis.expirationTime


2. Start the application!

In order to run all unit tests and start the application, use the command:
mvn clean install spring-boot:run

The application should start up at: 
http://localhost:8080

As mentioned in the exam instructions, the only endpoint available will be running at:
http://localhost:8080/api/recent_purchases/:username
