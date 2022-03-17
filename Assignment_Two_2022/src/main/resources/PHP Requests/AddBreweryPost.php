<?php
$url = "http://localhost:8888/brewery/Add/";

$curl = curl_init();
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_POST, true);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

$headers = array(
   "Accept: application/json",
   "Content-Type: application/json",
);
curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

$data = <<<DATA
{
    
    "name": "Limerick IT Brewery",
    "address1": "Moylish",
    "address2": "Campus",
    "city": "Limerick",
    "state": "Munster",
    "code": "94151",
    "country": "Ireland",
    "phone": "086-1471111",
    "website": "http://tus.ie",
    "image": "no_image.jpg",
    "description": "Test Description",
    "add_user": 0,
    "last_mod": "2019-03-10T13:17:02.000+00:00",
    "credit_limit": 3597.95,
    "email": "brewery@tus.ie"
}
DATA;

curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

$resp = curl_exec($curl);
var_dump($resp);
curl_close($curl);


?>