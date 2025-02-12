##!/usr/bin/env bash
##
## Sample usage:
##
##   HOST=localhost PORT=7000 ./test-em-all.bash
##
#: ${HOST=localhost}
#: ${PORT=8443}
#: ${PROD_ID_REVS_RECS=1}
#: ${PROD_ID_NOT_FOUND=13}
#: ${PROD_ID_NO_RECS=113}
#: ${PROD_ID_NO_REVS=213}
#
#function assertCurl() {
#
#  local expectedHttpCode=$1
#  local curlCmd="$2 -w \"%{http_code}\""
#  local result=$(eval $curlCmd)
#  local httpCode="${result:(-3)}"
#  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"
#
#  if [ "$httpCode" = "$expectedHttpCode" ]
#  then
#    if [ "$httpCode" = "200" ]
#    then
#      echo "Test OK (HTTP Code: $httpCode)"
#    else
#      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
#    fi
#  else
#    echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
#    echo  "- Failing command: $curlCmd"
#    echo  "- Response Body: $RESPONSE"
#    exit 1
#  fi
#}
#
#function assertEqual() {
#
#  local expected=$1
#  local actual=$2
#
#  if [ "$actual" = "$expected" ]
#  then
#    echo "Test OK (actual value: $actual)"
#  else
#    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
#    exit 1
#  fi
#}
#
#function testUrl() {
#  url=$@
#  if $url -ks -f -o /dev/null
#  then
#    return 0
#  else
#    return 1
#  fi;
#}
#
#function waitForService() {
#  url=$@
#  echo -n "Wait for: $url... "
#  n=0
#  until testUrl $url
#  do
#    n=$((n + 1))
#    if [[ $n == 100 ]]
#    then
#      echo " Give up"
#      exit 1
#    else
#      sleep 3
#      echo -n ", retry #$n "
#    fi
#  done
#  echo "DONE, continues..."
#}
#
#function testCompositeCreated() {
#
#    # Expect that the Product Composite for productId $PROD_ID_REVS_RECS has been created with three recommendations and three reviews
#    if ! assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
#    then
#        echo -n "FAIL"
#        return 1
#    fi
#
#    set +e
#    assertEqual "$PROD_ID_REVS_RECS" $(echo $RESPONSE | jq .productId)
#    if [ "$?" -eq "1" ] ; then return 1; fi
#
#    assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
#    if [ "$?" -eq "1" ] ; then return 1; fi
#
#    assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")
#    if [ "$?" -eq "1" ] ; then return 1; fi
#
#    set -e
#}
#
#function waitForMessageProcessing() {
#    echo "Wait for messages to be processed... "
#
#    # Give background processing some time to complete...
#    sleep 1
#
#    n=0
#    until testCompositeCreated
#    do
#        n=$((n + 1))
#        if [[ $n == 40 ]]
#        then
#            echo " Give up"
#            exit 1
#        else
#            sleep 6
#            echo -n ", retry #$n "
#        fi
#    done
#    echo "All messages are now processed!"
#}
#
#function recreateComposite() {
#  local productId=$1
#  local composite=$2
#
#  assertCurl 202 "curl -X DELETE $AUTH -k https://$HOST:$PORT/product-composite/${productId} -s"
#  assertEqual 202 $(curl -X POST -s -k https://$HOST:$PORT/product-composite -H "Content-Type: application/json" -H "Authorization: Bearer $ACCESS_TOKEN" --data "$composite" -w "%{http_code}")
#}
#
#function setupTestdata() {
#
#  body="{\"productId\":$PROD_ID_NO_RECS"
#  body+=\
#',"name":"product name A","weight":100, "reviews":[
#  {"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
#  {"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
#  {"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
#]}'
#  recreateComposite "$PROD_ID_NO_RECS" "$body"
#
#  body="{\"productId\":$PROD_ID_NO_REVS"
#  body+=\
#',"name":"product name B","weight":200, "recommendations":[
#  {"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
#  {"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
#  {"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
#]}'
#  recreateComposite "$PROD_ID_NO_REVS" "$body"
#
#
#  body="{\"productId\":$PROD_ID_REVS_RECS"
#  body+=\
#',"name":"product name C","weight":300, "recommendations":[
#      {"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
#      {"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
#      {"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
#  ], "reviews":[
#      {"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
#      {"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
#      {"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
#  ]}'
#  recreateComposite "$PROD_ID_REVS_RECS" "$body"
#
#}
#
#set -e
#
#echo "Start Tests:" `date`
#
#echo "HOST=${HOST}"
#echo "PORT=${PORT}"
#
#if [[ $@ == *"start"* ]]
#then
#  echo "Restarting the test environment..."
#  echo "$ docker compose down --remove-orphans"
#  docker compose down --remove-orphans
#  echo "$ docker compose up -d"
#  docker compose up -d
#fi
#
##waitForService curl -k https://$HOST:$PORT/actuator/health
#
#ACCESS_TOKEN=$(curl  -k http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write)
#echo ACCESS_TOKEN=$ACCESS_TOKEN
#AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""
#
## Verify access to Eureka and that all four microservices are registered in Eureka
#assertCurl 200 "curl -H "accept:application/json" -k https://$HOST:$PORT/eureka/api/apps -s"
#assertEqual 6 $(echo $RESPONSE | jq ".applications.application | length")
#
#setupTestdata
#
#waitForMessageProcessing
#
## Verify that a normal request works, expect three recommendations and three reviews
#assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
#assertEqual $PROD_ID_REVS_RECS $(echo $RESPONSE | jq .productId)
#assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
#assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")
#
## Verify that a 404 (Not Found) error is returned for a non-existing productId ($PROD_ID_NOT_FOUND)
#assertCurl 404 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_NOT_FOUND -s"
#assertEqual "No product found for productId: $PROD_ID_NOT_FOUND" "$(echo $RESPONSE | jq -r .message)"
#
## Verify that no recommendations are returned for productId $PROD_ID_NO_RECS
#assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_NO_RECS -s"
#assertEqual $PROD_ID_NO_RECS $(echo $RESPONSE | jq .productId)
#assertEqual 0 $(echo $RESPONSE | jq ".recommendations | length")
#assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")
#
## Verify that no reviews are returned for productId $PROD_ID_NO_REVS
#assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_NO_REVS -s"
#assertEqual $PROD_ID_NO_REVS $(echo $RESPONSE | jq .productId)
#assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
#assertEqual 0 $(echo $RESPONSE | jq ".reviews | length")
#
## Verify that a 422 (Unprocessable Entity) error is returned for a productId that is out of range (-1)
#assertCurl 422 "curl $AUTH -k https://$HOST:$PORT/product-composite/-1 -s"
#assertEqual "\"Invalid productId: -1\"" "$(echo $RESPONSE | jq .message)"
#
## Verify that a 400 (Bad Request) error error is returned for a productId that is not a number, i.e. invalid format
#assertCurl 400 "curl $AUTH -k https://$HOST:$PORT/product-composite/invalidProductId -s"
#assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"
#
## Verify that a request without access token fails on 401, Unauthorized
#assertCurl 401 "curl -k https://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
#
## Verify that the reader - client with only read scope can call the read API but not delete API.
#READER_ACCESS_TOKEN=$(curl -k https://reader:secret-reader@$HOST:$PORT/oauth2/token -d grant_type=client_credentials -d scope="product:read" -s | jq .access_token -r)
#echo READER_ACCESS_TOKEN=$READER_ACCESS_TOKEN
#READER_AUTH="-H \"Authorization: Bearer $READER_ACCESS_TOKEN\""
#
#assertCurl 200 "curl $READER_AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
#assertCurl 403 "curl -X DELETE $READER_AUTH -k https://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
#
## Verify access to Swagger and OpenAPI URLs
#echo "Swagger/OpenAPI tests"
#assertCurl 302 "curl -ks  https://$HOST:$PORT/openapi/swagger-ui.html"
#assertCurl 200 "curl -ksL https://$HOST:$PORT/openapi/swagger-ui.html"
#assertCurl 200 "curl -ks  https://$HOST:$PORT/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config"
#assertCurl 200 "curl -ks  https://$HOST:$PORT/openapi/v3/api-docs"
#assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
#assertEqual "https://$HOST:$PORT" "$(echo $RESPONSE | jq -r '.servers[0].url')"
#assertCurl 200 "curl -ks  https://$HOST:$PORT/openapi/v3/api-docs.yaml"
#
#if [[ $@ == *"stop"* ]]
#then
#    echo "We are done, stopping the test environment..."
#    echo "$ docker compose down"
#    docker compose down
#fi
#
#echo "End, all tests OK:" `date`
#
#
#curl -X Get http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
#
#
#curl -X POST http://localhost:9999/oauth2/token \
#  -H "Content-Type: application/x-www-form-urlencoded" \
#  -d
#  -d "grant_type=authorization_code" \
#  -d "code=T6g1srJlwkB4Eiwaopx2fMh05khPFZ8AFen4hoHtLD22OhpSLfYvcDJlsbs5aI6vtGeDr_Om9CRC5qrCBAEah1vmVKcBnT_5wI4MM0tSfNIfOqZORta2YhDvvZuOUr3y"\
#  -d "redirect_uri=https://localhost:8443/oauth2/code"
# http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
#
#curl -X POST "http://localhost:9999/oauth2/token" \
#  -H "Authorization: Basic Y2hhdTp7bm9vcH0xMjM=" \
#  -H "Content-Type: application/x-www-form-urlencoded" \
#  -d "grant_type=authorization_code" \
#  -d "code=eyJraWQiOiJkYWExYWU5Mi01ZGYzLTQwMWItYWUyMC02ODk3NjYzNzI5NzkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjaGF1MiIsImF1ZCI6ImNoYXUiLCJuYmYiOjE3MzE2NTE1NDYsInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJvcGVuaWQiLCJwcm9kdWN0OnJlYWQiXSwicm9sZXMiOlsiVVNFUiJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojk5OTkiLCJleHAiOjE3MzE2NTUxNDYsImlhdCI6MTczMTY1MTU0NiwianRpIjoiZjcwYWZmMmUtMDNmMy00NThkLWI0NTYtM2Q5NTIyODEyNmVhIiwiZW1haWwiOiJ0ZXN0QGdtYWlsLmNvbSJ9.VvVuWj5M9Muk4dZV06YxTNWH1OL0F4wMDdZESy9AdFjxVUK3136wZiqKWwmZN5bS8N1xnGxpznCWfQpeD736mlEpelP9Akn21-vxSbXTRZp0kUC0xe38-7_jw6C7B0TgN8k-af8AUjnGlSHhZyYqRtAK0tkA5tcIrjxq1tD0yLLZwh8dlw5Dr4-lQ0F_hqlzVIkTTrhyJ1YvYruRrWOwnKMEqWwEuADd0niBWpeY8wpMRAPPVwpDfeoK9UzI1HEScty4M7LyfyRXBicHaOFb3kVTI9P6zixnl424vxyopd4uN12Zqz_XpNypeiw2pV9jOcCs_jPoER5pXpODPonzBQ" \
#  -d "redirect_uri=https://localhost:8443/oauth2/code"
#assertEqual "HALF_OPEN" "$(docker-compose exec -T product-composite curl -s http://product-composite:8080/actuator/health | jq -r .components.circuitBreakers.details.product.details.state)"
#
#
#
#unset ACCESS_TOKEN
#ACCESS_TOKEN=$(curl -k http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write  | jq -r .access_token)
#echo $ACCESS_TOKEN
#
  unset KUBECONFIG
  minikube start \
  --profile=chau \
  --memory=2000 \
  --cpus=4 \
  --disk-size=30g \
  --kubernetes-version=v1.26.1 \
  --driver=docker \
  --ports=8080:80 --ports=8443:443 \
  --ports=30080:30080 --ports=30443:30443
minikube delete -p chau
minikube start -p chau --kubernetes-version=v1.26.1
minikube profile chau
minikube addons enable ingress
minikube addons enable metrics-server
 set up namespace
kubectl create namespace hands-on
##kubectl config set-context $(kubectl config current-context) --namespace=firstattempts
#kubectl config set-context $(kubectl config current-context) --namespace=handson
##set up nginx for port 80 1 for deploy and 1 for server
#kubectl apply -f Kubernetes/first-attempts/nginx-deployment.yaml
#kubectl apply -f Kubernetes/first-attempts/nginx-service.yaml
#kubectl run -i --rm --restart=Never curl-client --image=curlimages/curl --command -- curl -s 'http://nginx-service:80'
#
#for f in components/*; do helm dependency update $f; done
##clone cofig-repo in dicrect
#ln -s ../../../../config-repo config-repo
#helm dep ls Kubernetes/helm/environments/dev-env/
#
#helm dependency update environments/dev-env
# helm template Kubernetes/helm/components/product -s templates/deployment.yaml
#
#eval $(minikube docker-env)
#docker pull mysql:8.0.32
#docker pull mongo:6.0.4
#docker pull rabbitmq:3.11.8-management
#docker pull openzipkin/zipkin:2.24.0
#helm template Kubernetes/helm/environments/dev-env
##check result before installing chars in Kubernetes
#helm install --dry-run --debug hands-on-dev-env \
#Kubernetes/helm/environments/dev-env
#helm dep ls Kubernetes/helm/environments/dev-env/
##install
#helm install hands-on \
#Kubernetes/helm/environments/dev-env \
#-n hands-on \
#--create-namespace
#
#helm install --debug hands-on-dev-env \
#Kubernetes/helm/environments/dev-env
#
#helm install hands-on-dev-env \
#Kubernetes/helm/environments/dev-env \
#-n hands-on \
#--create-namespace

docker containers
eval $(minikube docker-env)
docker-compose up -d --build
docker pull mysql:8.0.32
docker pull mongo:6.0.4
docker pull rabbitmq:3.11.8-management
docker pull openzipkin/zipkin:2.24.0

helm template Kubernetes/helm/environments/dev-env -s templates/deployment.yaml
helm template Kubernetes/helm/components/mongodb -s templates/service.yaml

helm template Kubernetes/helm/environments/dev-env

minikube start
minikube -p chau docker-env
docker-compose build

eval $(minikube docker-env)

for f in components/*; do helm dependency update $f; done
for f in Kubernetes/helm/components/*; do helm dep up $f; done
for f in Kubernetes/helm/environments/*; do helm dep up $f; done
helm upgrade hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on

helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace

helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace
kubectl get pods -n hands-on
docker-compose build
helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace

helm upgrade hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on

docker-compose build

eval $(minikube docker-env)
kubectl delete namespace hands-on
for f in Kubernetes/helm/components/*; do helm dep up $f; done
for f in Kubernetes/helm/environments/*; do helm dep up $f; done
helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace
kubectl get pods -n hands-on

docker push hands-on/auth-server:latest

docker-compose build
kubectl delete namespace hands-on
for f in Kubernetes/helm/components/*; do helm dep up $f; done
for f in Kubernetes/helm/environments/*; do helm dep up $f; done
helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on  --create-namespace

helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace

helm create hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --creat-namespace

helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on
helm upgrade hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on
helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace

helm dep ls Kubernetes/helm/environments/dev-env/

helm install --dry-run --debug hands-on-dev-env Kubernetes/helm/environments/dev-env
helm upgrade hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on
helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace

helm install hands-on-dev-env Kubernetes/helm/environmen ts/dev-env -n hands-on
kubectl config set-context $(kubectl config current-context) --namespace=hands-on
kubectl get pods -n hands-on --watch
kubectl get pods -n hands-on

#chau has a default namespace is default`
kubectl delete pods --all -n hands-on
kubectl delete pod auth-server-5bc6786b4f-xwdxc-n hands-on

# phai them minikube
kubectl wait --timeout=600s --for=condition=ready pod --all
# check status of current podkubectl describe pod auth-server-67b9658567-k99rp
helm uninstall hands-on-dev-env -n hands-on
kubectl logs product-composite-7848c5bcd-kvjfk  -c product-composite -n hands-on
kubectl logs auth-server-64d6bb88cc-6tcbt  -c auth-service -n hands-on
#config-server-5b49876ff5-9b2qn
kubectl edit deployment auth-server -n hands-on

kubectl set image deployment/gateway gateway=<new-image> -n hands-on

helm install hands-on-dev-env Kubernetes/helm/environments/dev-env -n hands-on --create-namespace
kubectl config set-context $(kubectl config current-context) --namespace=hands-on

kubectl set image deployment/my-deployment my-container=my-image:v2

kubectl run -i --rm --restart=Never curl-client --image=curlimages/curl --command -- curl -s 'http://nginx-service:80'

helm template Kubernetes/helm/components/config-server -s templates/deployment.yaml



helm upgrade auth-server Kubernetes/helm/components/auth-server --namespace hands-on
helm upgrade hands-on-dev-env ./auth-server

kubectl describe pod auth-server-64d6bb88cc-6tcbt  -n hands-on

kubectl logs  pod config-server-6bcbf9fc9b-59rdv  -n hands-on

helm upgrade hands-on-dev-env -n hands-on Kubernetes/helm/environments/dev-env --wait

#check network giua auth-server va config-server
kubectl exec -it auth-server-5bc6786b4f-dsjnq   -n hands-on -- ping config-server.hands-on.svc.cluster.local

kubectl exec -it auth-server-5bc6786b4f-dsjnq -n hands-on -- curl http://config-server.hands-on.svc.cluster.local:8888


kubectl port-forward <config-server-pod-name> 8888:8888 -n hands-on



