<?php 
session_start();


	$invalid = false;

	include("connection.php");
	include("functions.php");


	if($_SERVER['REQUEST_METHOD'] == "POST")
	{
		//something was posted
		$user_name = $_POST['user_name'];
		$password = $_POST['password'];
		$full_name = $_POST['full_name'];

		if(!empty($user_name) && !empty($password) && !is_numeric($user_name) &&!empty($full_name))
		{

			//save to database
			$user_id = random_num(20);
			$query = "insert into users (full_name, user_name,password) values ('$full_name','$user_name','$password')";

			mysqli_query($con, $query);

			header("Location: login.php");
			die;
		}else
		{
			$invalid = true;
		}
	}
?>


<!DOCTYPE html>
<html>
<head>
	<title>Signup</title>
	<link rel="stylesheet" href="../css/loginStyle.css" />
</head>
<body>

	<div id="loginBox">
		
		<form method="post">
			<div style="font-size: 25px;margin: 10px; padding: 10px;color: black;">Signup</div><br>

			<div style="margin: 10px; padding: 5px;">Full name</div>
			<input id="text" type="text" name="full_name">
			
			<div style="margin: 10px; padding: 5px;">Username</div>
			<input id="text" type="text" name="user_name">
			
			<div style="margin: 10px; padding: 5px;">Password</div>
			<input id="text" type="password" name="password"><br>

			<div style="color: red; margin: 30px;">
			<?php
			if($invalid == true){
				echo "Please enter valid information!";
			}
			?>
			</div>

			<input id="button" type="submit" value="Signup"><br>

			<a href="login.php">Click to Login</a><br><br>
		</form>
	</div>
</body>
</html>