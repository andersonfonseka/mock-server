# mock-server

- < 10 seconds
- No code
- Intercepts your real server, collects a returned response and works in offline mode.

![alt tag](https://github.com/andersonfonseka/mock-server/blob/master/images/general-overview.png)

How to start:

- Download <b>mock-server-0.0.1.zip</b>, unzip into any folder as you wish.
- On Windows: double-click on mock-server-0.0.1-SNAPSHOT.jar 

![alt tag](https://github.com/andersonfonseka/mock-server/blob/master/images/mock-server.png)

- <b>Router name</b> - when you want to call as a different URL, e.g.: http://locahost:3000/myserver
- <b>Port number</b> - port number.
- <b>Real server host</b> - assigns the URL real server for intercept all requests/responses.
- <b>Foward mode</b> 
		- when it's on, intercepts a request, collects the response, and stores into fakeServer folder.
		- when it's off, delivers the response from fakeServer folder.
- <b>Header propagation</b> some calls needs to propagate information within http header such as: tokens, pagination, and so on. 

A service request by Postman

![alt tag](https://github.com/andersonfonseka/mock-server/blob/master/images/postman.png)


