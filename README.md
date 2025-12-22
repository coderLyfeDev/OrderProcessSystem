Order Processing System

Features
1. Create an order
2. Obtain the status of an order given an order ID
3. Update the Status of an order given an Order ID

------Publish/Subscriber messaging pattern-------
Uses Spring Boot application events to publish events allowing services to subscribe to them in Asynchronous fashion.

------Starting the application-------
To Test you can use 1 of 3 methods.
1. run a .jar file that will start the backend service, run the startup queries and launch a Java Swing GUI.
   (Upon testing this application it was discovered that extra libraries may need to be install for swing. 
It worked without that step on my mac)
   - Unzip Swing-jar
   - You must have java 17+ (https://www.oracle.com/java/technologies/downloads/#jdk25-mac)
   - run java -jar demo-0.0.1-SNAPSHOT.jar
   - The above command will start the backend, insert initial queries and open a swing GUI.
   ******************************************************************
2. Run Dockerfile**********Preferred method*************
   - You will need Docker installed on your machine (https://www.docker.com/get-started/)
   - start the docker daemon by opening docker desktop or through the terminal
   - Unzip the file named docker-react.zip
   - In your terminal or command line, cd to docker-react
   - run docker build -t order-app .
   - run docker run --name order-app -p 8080:8080 -p 3000:3000 order-app
   - view logs - docker logs -f order-app
   - visit http://localhost:3000/ in your web browser to test the application
3. Run the backend jar file and test endpoints using Postman
   - You must have java 17+ (https://www.oracle.com/java/technologies/downloads/#jdk25-mac)
   - Must download postman or something similar (https://www.postman.com/downloads/)
   - Unzip the file named backend-only.zip
   - You must have java 17+
   - In your terminal or command line, cd to backend-only
   - run java -jar demo-0.0.1-SNAPSHOT.jar

