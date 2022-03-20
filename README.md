<h1 align="center">
  <img src="https://user-images.githubusercontent.com/23299540/159172501-b8a89589-d664-40bc-b0fb-ab88e8560b88.png" alt="Buddy Matcher" height="200px"></a>
  <br>
  Class Buddy Matcher
  <br>
</h1>
<h4 align="center">Group project for SOFTENG 701 Team 1 (Backend)</h4>
<p align="center">
<br>

Class Buddy Matcher - Group project for SOFTENG 701 Team 1 (Backend). 

This project comprises the backend segment (API + database) of the Buddymatcher application. It was created using Spring Boot and uses H2 for its database system. 

## How to run the app
The run configuration should look like the image below. The API can be accessed at [localhost:8080/api/](http://localhost:8080/api/)

![image](https://user-images.githubusercontent.com/62087759/159146429-75c1de2f-90c9-41e7-b453-30a96df5774c.png)


### Credentials 
The app's login system is done through Sign In With Google, and hence this project's API is associated with a google account. For the credentials for this account, please refer to one of the original team members. 

### Authentication
Details on the authentication flow and use of tokens can be found in the frontend wiki [here](https://github.com/SE701-T1/frontend/wiki/Authentication-Flow).  

## Testing the app 

### Integration testing 
Before creating a pull request, you should make sure your backend still passes all the tests with your changes. You can do this with Intellij by opening the Maven popup on the right side of the IDE and running "test" under Lifecycle. You don't need to have the app running before this step - the testing will take care of it. 


![image](https://user-images.githubusercontent.com/62087759/159145986-9206fe11-7571-45cc-a03a-f53f2f8ecc3d.png)

 If successful, the output should look like this: 
 
 ![image](https://user-images.githubusercontent.com/62087759/159146029-282a59f9-b896-4b61-b8a1-598740976a92.png)
 
 Note, while all requests to endpoints are normally prehandled, this is bypassed in the endpoint tests so no need to worry about authentication. 


### Manual testing 
If you would like to manually test the app, you can hit any endpoint you'd like using Postman or Swagger. To test the API this way, you will need to pass in a custom JWT in your request. See Authentication above for how it works. 



## Project structure
Information about the backend project structure can be found [here](https://github.com/SE701-T1/backend/wiki/Backend-Structure).
