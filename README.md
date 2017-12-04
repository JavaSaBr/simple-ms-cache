# Simple cache microservice

Задача : разработать МС кеша. С данным МС может работать большое кол-во систем. Время хранения записи не устанавливается, 
однако спустя некоторое время запись можно пометить как ненужную/удалить.  

## API

- POST _/_ 
  - params: 
    - 'key' - for key
    - 'value' - for value
- GET _/{key}_
  - return 404 if not found, or  value if it's associated with key 

### Examples :
 
#### curl -v -X POST -H "Content-Type: application/json" -d '{"key": "2", "value": "4"}' "http://localhost:8080"
     Note: Unnecessary use of -X or --request, POST is already inferred.
     * Rebuilt URL to: http://localhost:8080/
     *   Trying 127.0.0.1...
     * Connected to localhost (127.0.0.1) port 8080 (#0)
     > POST / HTTP/1.1
     > Host: localhost:8080
     > User-Agent: curl/7.47.0
     > Accept: */*
     > Content-Type: application/json
     > Content-Length: 26
     > 
     * upload completely sent off: 26 out of 26 bytes
     < HTTP/1.1 200 
     < Content-Type: application/json;charset=UTF-8
     < Transfer-Encoding: chunked
     < Date: Mon, 13 Nov 2017 09:45:09 GMT
     < 
     * Connection #0 to host localhost left intact
     {"key":"2"}

#### curl -v "http://localhost:8080/1"                    
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > GET /1 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.47.0
    > Accept: */*
    > 
    < HTTP/1.1 200 
    < Content-Type: application/json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Mon, 13 Nov 2017 09:38:19 GMT
    < 
    * Connection #0 to host localhost left intact
    {"key":"1","val":"2"}

#### curl -v "http://localhost:8080/2"
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > GET /2 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.47.0
    > Accept: */*
    > 
    < HTTP/1.1 404 
    < Content-Type: application/json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Mon, 13 Nov 2017 09:36:04 GMT
    < 
    * Connection #0 to host localhost left intact
    {"key":"2"}    
 
 
