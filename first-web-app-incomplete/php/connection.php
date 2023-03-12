<?php

$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$dbname = "pet_supplies";


//creates a mysql connection
if(!$con = mysqli_connect($dbhost,$dbuser,$dbpass,$dbname))
{
	die("failed to connect!");
}


?>