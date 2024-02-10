# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

### Phase 2 Server Design Diagram
[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND1i7AEzm9XnJjAEFOkGgbd75Yf+dncZeDXSYyaYI8Xm99wdH5hRdQFyDBCFZQ+O14URL1o0dWNayGd9ThTFB4nZKA311NDCUNS5vzuU0UFZC0rUfT4EOA51IldSVpSg215S7CsZ3VTUy2op043PVDqXQ0t0zYnCqWTfDkNPISrW7LNT2QxdGxgVt21KVjM17NB+1McwrFsMwUFDSd2EsZgbD8AIgmQBt-ikxIZAg9JgU3bcuF3exhMzKtqBrPjcIE-D4lvEzLTmdT0AOHUxKvWkjSIxlDBQBAnhQcjrXCtAgJ4kUwIc8EnLTGS2NgiAkS440QL86KDWJUlySi3NP0LKIfNXEkyW8-pmsU5s20wPsB104dpg0Cc3BgABxO1MXMucrJCZhBjshIJuBLJcnYO1igyzrfMiBqP0Em83Cm6ouDCzyItExr0K-RCfz-NKLqKzMsqQ0CgQgyFCvLTMSrK-1uPeqqbsCmA0tk67DokuL7uI38ngUMlTtUOY3oxHLPvBb7NrO-7fztIGMeag68PzVMUYSTIobJgslpXeJKep+TuusjBetUkomc07TBz0mxsAsKBsGS+AzQMFHZ0shc2cW5dWviZI0nWnJcZQbbLrQDs1YlO0TxXf5SYC8mMJgZlSMCFHnt+9A6h1u1IsTaGTbumj7gRsAnoy9GQLosCQWxqEMvx8r4t4-andp1MIZEo3xJd2G3ZI1krft6ofdogFxXdT004MDiCeqInKoji8o9NtWxRkGnjbp+WYgeO0q92s9Bh65S221puZB5wah1sRwktvbwYAAKQge9JpgmxtAQUAgxlhbbIZtdnhVtWNZe9AO0UuAIFvKA7e7-XWsNyPa9TAArCe0CtjK6l3-foCP6oq8dsuL4ImAKoSx7-Wt2SGc4x+yxpBH6skQ6Ax-uHFC-l47Xhjl5OOMUv7QIeE8VO3cgGY3AoHQuBgESlXwcXZ0INnbXhRjXeBsVCJwwSlYLQls7RzBRtgj6bpGJT2qGGTUasSEwOQTVGA7V6rn2oYaemCthF1RbgpWWHN+paT7vzMwwBnCIFIrAYA2ARaEEvFLecill5SISHlNazlchGFkWQ8u8QNBJRSmjKhKDXY8TsQ480ZInpsJAd-RywJwHFUIQDKM0DkKCKOiI5xTVaFJ0SslTxCAsLnR8VnPx+UAmLExME+CYSSZiJQbVDqESYaxLcfElKSMEAoxSfwnBZiCpq2yXBPheSbGfyKaIj+4jJIryiSzNu8iO4tkUf2IAA)
