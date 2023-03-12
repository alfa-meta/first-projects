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

		if(!empty($user_name) && !empty($password) && !is_numeric($user_name))
		{

			//read from database
			$query = "select * from users where user_name = '$user_name' limit 1";
			$result = mysqli_query($con, $query);

			if($result)
			{
				if($result && mysqli_num_rows($result) > 0)
				{

					$user_data = mysqli_fetch_assoc($result);
					
					if($user_data['password'] === $password)
					{

						$_SESSION['id'] = $user_data['id'];
						header("Location: index.php");
						die;
					}
				}
			}
			
			$invalid = true;
		}else
		{
			$invalid = true;
		}
	}

?>


<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="../css/loginStyle.css" />
	<title>Login</title>
</head>
<body>
	<div id="loginBox">
		
		<form method="post">
			<div style="font-size: 25px;margin: 10px; padding: 10px; color: Black;">Login</div><br>

			<div style="margin: 10px; padding: 5px;">Username</div>
			<input id="text" type="text" name="user_name">

			<div style="margin: 10px; padding: 5px;">Password</div>
			<input id="text" type="password" name="password"><br>

			<div style="color: red; margin: 30px;">
			<?php
			if($invalid == true){
				echo "Invalid Username and or Password!";
			}
			?>
			</div>

			<input id="button" type="submit" value="Login"><br>

			<a href="signup.php">Click to Signup</a><br><br>
		</form>
	</div>
</body>
</html>