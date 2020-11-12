Hibernate Spring JPA basic staff.

- How run the application 

```
./mvnw spring-boot:run
```

- How run the tests

```
./mvnw clean install
```

```
[INFO] Results:
[INFO] 
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  34.265 s
[INFO] Finished at: 2020-11-12T13:29:58+02:00
[INFO] ------------------------------------------------------------------------
```

- REST API Details
    - Get All Players
        ```
        curl -X GET http://localhost:8080/players
        ```
    - Get Player By ID 
        ```  
        curl -X GET http://localhost:8080/players/1
        ```
    - Delete Player By ID
        ```  
        curl -X DELETE http://localhost:8080/players/1
       ```
#Find the explanation for some tests:

- **HibernateApplicationTests#shouldDeleteThePlayer**

```
OpenEntityManagerInViewInterceptor 
```

![alt text](https://vladmihalcea.com/wp-content/uploads/2016/05/opensessioninview.png)

* The OpenSessionInViewFilter calls the openSession method of the underlying SessionFactory and obtains a new Session.
* The Session is bound to the TransactionSynchronizationManager.
* The OpenSessionInViewFilter calls the doFilter of the javax.servlet.FilterChain object reference and the request is further processed
* The DispatcherServlet is called, and it routes the HTTP request to the underlying PostController.
* The PostController calls the PostService to get a list of Post entities.
* The PostService opens a new transaction, and the HibernateTransactionManager reuses the same Session that was opened by the OpenSessionInViewFilter.
* The PostDAO fetches the list of Post entities without initializing any lazy association.
* The PostService commits the underlying transaction, but the Session is not closed because it was opened externally.
* The DispatcherServlet starts rendering the UI, which, in turn, navigates the lazy associations and triggers their initialization.
* The OpenSessionInViewFilter can close the Session, and the underlying database connection is released as well.

At a first glance, this might not look like a terrible thing to do, but, once you view it from a database perspective, a series of flaws start to become more obvious.

The service layer opens and closes a database transaction, but afterward, there is no explicit transaction going on. For this reason, every additional statement issued from the UI rendering phase is executed in auto-commit mode. Auto-commit puts pressure on the database server because each statement must flush the transaction log to disk, therefore causing a lot of I/O traffic on the database side. One optimization would be to mark the Connection as read-only which would allow the database server to avoid writing to the transaction log.

There is no separation of concerns anymore because statements are generated both by the service layer and by the UI rendering process. Writing integration tests that assert the number of statements being generated requires going through all layers (web, service, DAO) while having the application deployed on a web container. Even when using an in-memory database (e.g. HSQLDB) and a lightweight web server (e.g. Jetty), these integration tests are going to be slower to execute than if layers were separated and the back-end integration tests used the database, while the front-end integration tests were mocking the service layer altogether.

Last but not least, the database connection is held throughout the UI rendering phase which increases connection lease time and limits the overall transaction throughput due to congestion on the database connection pool. The more the connection is held, the more other concurrent requests are going to wait to get a connection from the pool.

Disabled this interceptor just add properties:

```
spring.jpa.open-in-view=false
```

## Read-life stories

[Spring Boot: Open Session In View caused cache problems](https://app-tinyurl.herokuapp.com/url/bb)

[Spring Boot Best Practice – Disable OSIV to start receiving LazyInitializationException warnings again](https://app-tinyurl.herokuapp.com/url/ba)

- **RepositoryUtilsTest#shouldRemovePlayer**

To recap that let's jump into the test classes.
In addition, I encourage you to and some reference explanation in the code:

```
DefaultPersistEventListener#onPersist -> switch DELETED
PersistenceContext -> StatefulPersistenceContext field EntityEntryContext
```