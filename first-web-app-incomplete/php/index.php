<?php

 
session_start();


	include("connection.php");
	include("functions.php");

    $user_data = check_login($con);

function adminPage(){
	echo 'Hello';
	$user_type = $_POST['user_type'];
	if($user_type == 'admin'){
		header("Location: admin.php");
	}

}


?>


<!DOCTYPE>
<html>
<head>
	<meta charset ="UTF-8">
	<title>Pet Supplies</title>
	<meta name="description" content="Pet Supplies, a pet shop for all you need pets local to redacted.">
	<meta name ="author" content="redacted">
	<!--Relative path to style.css file><!-->
	<link rel="stylesheet" href="../css/websiteStyle.css" />
</head>
<body>
	<!--This is a comment><!-->
	<div>
		<div class="header">
		<img src="../images/banner.jpg" alt="Pet supplies banner" width="1400" height="300">
		</div>
	</div>
	
	<div>
		<div class="navmenu">
			<a href="index.php">Home</a>
			<a href="../html/store.html">Store</a>
			<a href="../html/contact.html">Contact Us</a>
			<!--Sets the 2nd to last element to the name of the user.><-->
			<a><?php if (isset($user_data['full_name'])) { echo $user_data['full_name'];} else { echo "<a href=$loginUrl>Login<a>";  } ?></a> 
			<a href="logout.php">Logout</a>
		</div>
	</div>
	
	<div>
		<div class="container">
		
			<div class="content-left">
				<p><h1>Left Main </h1></p>
			</div>
			
			<div class="content-large">
				<table>
					<tr>
						<th><h1>Pet Supplies Homepage</h1></th>
					</tr>
						<th><h3>Welcome <?php echo $user_data['full_name']; ?> to Pet Supplies redacted.</h3></th>
					<tr>
					</tr>
						<th>
							<img src="../images/dog-leash.jpg" width="400" height="200">
						</th>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
						<th>We are happy to welcome you to our humble online store. Here you will be able to buy many items for your pet needs.

						We hope you have a wonderful day - Pet Supplies Team.
						</th>
					</tr>
				</table>
			</div>
			
			<div class="content-right">
				<p><h1>Right Main</h1></p>
			</div>
		</div>
	</div>	
	<div class="footer">
		<h2>If you want to visit us, you can find us here: Richmond Road, Bradford BD7 1DP. Call us on: redacted</h2>
	</div>
</body>
</html>