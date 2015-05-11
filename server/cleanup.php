
<?php
	
	/*
		Author - Ivan Liljeqvist 
		Version - 10-05-2015

		All this PHP file does is removing user_info.txt.
		By doing this Muzikk erases previous logged in users before logging in a new user.
	*/

		
	unlink('user_info.txt') or die("couldnt unlink file");


?>