: ${HOST=localhost}
: ${PORT=8080}
: ${PROD_ID_REVS_RECS=1}
: ${PROD_ID_NOT_FOUND=13}
: ${PROD_ID_NO_RECS=113}
: ${PROD_ID_NO_REVS=213}
if [[ $@ == *"start"* ]]
then
echo "Restarting the test environment..."
echo "$ docker-compose down --remove-orphans"
docker-compose down --remove-orphans
echo "$ docker-compose up -d"
docker-compose up -d
fi
function testUrl() {
url=$@
if curl $url -ks -f -o /dev/null
then
return 0
else
return 1
fi;
}
function waitForService() {
url=$@
echo -n "Wait for: $url... "
n=0
until testUrl $url
do
n=$((n + 1))
if [[ $n == 100 ]]
then
echo " Give up"
exit 1
else
sleep 3
echo -n ", retry #$n "
fi
done
echo "DONE, continues..."
}

if [[ $@ == *"stop"* ]]
then
echo "We are done, stopping the test environment..."
echo "$ docker-compose down"
docker-compose down
fi
waitForService http://localhost:8080/product-composite/1
echo "Swagger/OpenAPI tests"
assertCurl 302 "curl -s  http://$HOST:$PORT/openapi/swagger-ui.html"
assertCurl 200 "curl -sL http://$HOST:$PORT/openapi/swagger-ui.html"
assertCurl 200 "curl -s  http://$HOST:$PORT/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config"
assertCurl 200 "curl -s  http://$HOST:$PORT/openapi/v3/api-docs"
assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
assertEqual "http://$HOST:$PORT" "$(echo $RESPONSE | jq -r '.servers[0].url')"
assertCurl 200 "curl -s  http://$HOST:$PORT/openapi/v3/api-docs.yaml"