
<?php
//The URL that we want to GET.
$url = 'http://localhost:8888/brewery/map/2';

//Use file_get_contents to GET the URL in question.
$contents = file_get_contents($url);

//If $contents is not a boolean FALSE value.
if($contents !== false){
    //Print out the contents.
    echo $contents;
}

?>