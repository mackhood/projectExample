# Project



### Requirements for build up the project
- Have docker installed


### Build up the entire project
- Download the repository, and enter to the /backend
- Execute the following comand inside the /backend folder:  
``` docker compose up -d```


### Urls

- For getting the rest endpoints you need the application running and access to the following url:

  ## Backend

  All listing endpoints can recieve query params:  page(default 1) and limit(default 5)

 Post
 http://localhost:8080/user
 body:
{
    "name": "german",
    "email": "test@hotmail.com"
}

response:
{
    "response": {
        "id": 3,
        "name": "test",
        "email": "test@hotmail.com"
    }
}



Get
http://localhost:8080/user/1

{
    "response": {
        "id": 1,
        "name": "german",
        "email": "test@hotmail.com"
    }
}

PATCH
http://localhost:8080/project/1/user/1

{
    "response": {
        "id": 1,
        "name": "gerProject9",
        "description": "test",
        "userProject": [
            {
                "id": 1,
                "name": "ger",
                "email": "ger@hotmail.com"
            }
        ]
    }
}


DELETE
http://localhost:8080/user/1

Delete the user and from all projects


DELETE
http://localhost:8080/project/1/user/1

Delete the user from the project

GET
http://localhost:8080/project/1


{
    "response": {
        "id": 1,
        "name": "gerProject9",
        "description": "test",
        "userProject": [
            {
                "id": 1,
                "name": "ger",
                "email": "ger@hotmail.com"
            }
        ]
    }
}


GET 
http://localhost:8080/projects

{
    "response": [
        {
            "id": 1,
            "name": "gerProject9",
            "description": "test"
        }
    ]
}
