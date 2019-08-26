## Messages board example

A simple application which allows to exchange messages via REST API and displays live messages on a message board.

### Usage

Build the application.
```bash
$ ./mvnw package
```

Start messages service in a first terminal window.
```bash
$ java -jar messages-service/target/messages-service.jar 
```

Start dashboard service in a second terminal window.
```bash
$ java -jar dashboard/target/dashboard.jar 
```

Open messages board in your [browser](http://localhost:8080).

Send messages in a third terminal window using HTTPie.
```bash
$ http POST :8081 sender=John text="Hello, World"
```

Or using cURL.
```bash
$ curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"sender": "John", "text": "Hello again"}' http://localhost:8081
```