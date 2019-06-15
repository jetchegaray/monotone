# monotone

images server , images processor , static images server. It is an Automata's computer that rules the whole planet. ;) 

take your images from S3 bucket  and adapt them to show you in the width and height you long for.  save them in cache for the next time that it will be called. 

if it does not find any image in the path return a default image located in https://github.com/jetchegaray/monotone/tree/master/src/main/resources .. with the same size as it got from parameters. 


Struture of your buckets 

I thinking of this struture for our entities in our project you can change it whatever you want 

 -- testing 
    -- commerces
      -- {commerceid}
        -- logo.png
        -- another.png
        -- otherlogo.png
        -- front.png
    -- users 
      -- {userId}
        -- logo.jpg
        -- back.webp
 
  -- production 
    -- commerces
      -- {commerceid}
        -- logo.png
        -- another.png
        -- otherlogo.png
        -- front.png
    -- users 
      -- {userId}
        -- logo.jpg
        -- back.webp
        

Set up configuration 


edit your bucket folders here 

https://github.com/jetchegaray/monotone/blob/master/src/main/java/ar/com/autominuto/monotone/utils/BucketsType.java 

in the example above ..  users  and commerces 

edit your credentials  here 

https://github.com/jetchegaray/monotone/blob/master/src/main/java/ar/com/autominuto/monotone/utils/AutominutoS3Credentials.java

edit your buckets name here 

https://github.com/jetchegaray/monotone/blob/master/src/main/java/ar/com/autominuto/monotone/utils/AutominutoS3BucketEnv.java


compile on root folder 


mvn clean install 


run it 

mvn spring-boot:run -Drun.jvmArguments="-Denvironment=dev"



how to call it 

http://localhost:8090/monotone/images/users/{userid}/logo.png?width=125&height=20&isologo=true

http://localhost:8090/monotone/images/users/{userid}/logo.png?isologo=true
