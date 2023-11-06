### BOOKSTORE API DOCUMENTATION

##Account

***Note - All the inputs will be in json format***

1) Register User
   http://localhost:8080/Account/User 
   
input -- {
           "userName": "lakshmipriya",
           "password": "Priya1234"      --- Password must be (1-UpperCase,1-LowerCase,1-Digit)
         }

output --   {
              "userId": "T1FW7",
  	      "userName": "lakshmipriya",
              "books": []
	    }       
         
  
   
2) Check User Authorization
   http://localhost:8080/Account/Authorized
   
 input --  {
             "userName": "lakshmipriya",
             "password": "Priya1234"
           }

 output --  {
                "userId": "T1FW7",
   	 	"token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJsYWtzaG1pcHJpeWEiLCJpYXQiOjE2OTc3MDA2NDgsImV4cCI6MTY5ODMwNTQ0OH0.vQUZvqvVri31Txp4A_XgLAy-MPeqUDSizX1t0c2LZr7qWw5pU33cXIfVSCysGggO",
    		"expires": "2023-10-26T07:30:48.556Z",
   	 	"status": "Success",
    		"result": "User authorized successfully"
   	     }
   


3) Delete User Account    (Authenticated -- can access by Admin and User)
   http://localhost:8080/Account/User/{userId}

 input --  http://localhost:8080/Account/User/T1FW7
   
   User Deleted Successfully


4) Get User Details     (Authenticated -- can access by Admin and User)
   http://localhost:8080/Account/User/{userId}
   
 input --  http://localhost:8080/Account/User/T1FW7 
 
 (If User Doesn't have any books)  
 output --     {
      		"userId": "T1FW7",
      		"userName": "lakshmipriya",
      		"books": []
    	       }       
         
 (If User have books)  
   {
    "userId": "T1FW7",
    "userName": "lakshmipriya",
    "books": [
        {
            "userId": "T1FW7",
            "isbn": "9781491950296",
            "title": "Programming JavaScript Applications",
            "subTitle": "Robust Web Architecture with Node, HTML5, and Modern JS Libraries",
            "author": "Eric Elliott",
            "publishDate": "2014-07-01T00:00:00",
            "publisher": "O'Reilly Media",
            "pages": 254,
            "description": "Take advantage of JavaScript's power to build robust web-scale or enterprise applications that are easy to extend and maintain. By applying the design patterns outlined in  this practical book, experienced JavaScript developers will learn how to write flex",
            "website": "http://chimera.labs.oreilly.com/books/1234000000262/index.html"
        },
        {
            "userId": "T1FW7",
            "isbn": "9781593277574",
            "title": "Understanding ECMAScript 6",
            "subTitle": "The Definitive Guide for JavaScript Developers",
            "author": "Nicholas C. Zakas",
            "publishDate": "2016-09-03T00:00:00",
            "publisher": "No Starch Press",
            "pages": 352,
            "description": "ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E",
            "website": "https://leanpub.com/understandinges6/read"
        }
    ]
}
  
  
  
##  BookStore


1) Add a Book   (Authenticated -- can access by Admin)
   http://localhost:8080/BookStore/Books
   
  input --   {
    	        "isbn": "9781593277574",
      		"title": "Understanding ECMAScript 6",
      		"subTitle": "The Definitive Guide for JavaScript Developers",
      		"author": "Nicholas C. Zakas",
      		"publishDate": "2016-09-03T00:00:00.000Z",
     		"publisher": "No Starch Press",
     		"pages": 352,
      		"description": "ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E",
    		"website": "https://leanpub.com/understandinges6/read"
             }
  
  
  output --   {
                "id": 8
    	        "isbn": "9781593277574",
      		"title": "Understanding ECMAScript 6",
      		"subTitle": "The Definitive Guide for JavaScript Developers",
      		"author": "Nicholas C. Zakas",
      		"publishDate": "2016-09-03T00:00:00.000Z",
     		"publisher": "No Starch Press",
     		"pages": 352,
      		"description": "ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E",
    		"website": "https://leanpub.com/understandinges6/read"
             }
   
   
2) Get All Books
   http://localhost:8080/BookStore/Books
     
  input --   http://localhost:8080/BookStore/Books
  
  output --  All the books present in the Books table will be the output.
   
   
3) Get Book by ISBN
   http://localhost:8080/BookStore/Books/{isbn}
   
  input --   http://localhost:8080/BookStore/Books/9781449325862
  
  output --  {
  		"id": 1,
    		"isbn": "9781449325862",
   		"title": "Git Pocket Guide",
    		"subTitle": "A Working Introduction",
    		"author": "Richard E. Silverman",
		"publishDate": "2020-06-04T08:48:39",
   		"publisher": "O'Reilly Media",
    		"pages": 234,
    		"description": "This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp",
    		"website": "http://chimera.labs.oreilly.com/books/1230000000561/index.html"
	    }
   

4) Delete a Book  (Authenticated -- can access by Admin)
   http://localhost:8080/BookStore/Books/{isbn}
   
  input --  http://localhost:8080/BookStore/Books/9781491950296
  
  
  
  output --  {
   		 "isbn": "9781593277571",
   		 "message": "Book deleted successfully"
             }
   
   
5) Add a Book to a User   (Authenticated -- can access by Admin and User)
   http://localhost:8080/BookStore/Book
   
  input --        {
  		    "userId": "T1FW7",
                    "collectionOfIsbns": [
    	               {
      		         "isbn": "9781491950296"      
    		       },
    		       {
       			 "isbn":"9781593277574"
   		       }
 		    ]
                  }
  
  output --     {
   		 "userId": "T1FW7",
   	 	 "message": "Book added successfully!"
		}
   
   
6) Update a Book for a User   (Authenticated -- can access by Admin and User)
   http://localhost:8080/BookStore/Book/{isbn}
   
  input --  http://localhost:8080/BookStore/Book/9781491950296
  
           {
   	     "userId":"7GWSJ",
             "isbn":"9781449331818"
	   }
 
  output --  {
               "code": 200,
               "message": "Book Updated Successfully"
             }
    
   
   
7) Delete a Book from a User   (Authenticated -- can access by Admin and User)
   http://localhost:8080/BookStore/Book

  input --     {
 		 "isbn": "9781491950296",
  		 "userId": "7GWSJ"
	       }

  output --   {
 		 "isbn": "9781491950296",
  		 "userId": "7GWSJ"
  		 "message" : "Book Deleted successfully"
	      }



8) Delete All Books From User (Authenticated -- can access by Admin and User)

  input --  http://localhost:8080/BookStore/Book/{userId}
  
  output -- {
 	      "userId": "PGZ6J",
    	      "message": "Books deleted successfully "
	    }


































