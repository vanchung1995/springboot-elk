# springboot-elk
ELK for Spring Boot Simple Project

When run elastic, we need to change this params of system: 

`sudo sysctl -w vm.max_map_count=262144`

If not, this error will happen:

```
 You must address the points described in the following [1] lines before starting Elasticsearch. For more information see [https://www.elastic.co/guide/en/elasticsearch/reference/8.16/bootstrap-checks.html]\nbootstrap check failure [1] of [1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]; for more information see [https://www.elastic.co/guide/en/elasticsearch/reference/8.16/bootstrap-checks-max-map-count.html
```
