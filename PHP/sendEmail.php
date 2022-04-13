<?php
$custNumber = $_REQUEST["customerNumber"];
$mobileNumber = $_REQUEST["mobileNumber"];

$to = "";
$subject = "Request For LPG";

$message = "Customer Number: ".$custNumber;
$message .= "<br>";
$message .= "Mobile Number: ".$mobileNumber;
$message .= "<br>";
$message .= "Request of booking for a new LPG!";
$headers = "MIME-Version: 1.0" . "\r\n";
$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";
$headers .= 'From: <>' . "\r\n";

header('Content-Type: application/json');
if(mail($to,$subject,$message,$headers)){
    echo json_encode(array("status"=>true));
}
else{
    echo json_encode(array("status"=>false));
}
?>