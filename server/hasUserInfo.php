<?php

	/*
		Author - Ivan Liljeqvist 
		Version - 10-05-2015

		This PHP service is used to check wether someone has logged in to Muzikk or not.
		If user_info.txt doesnt exist - it means that one one is logged in and we print 'no user'.
		If we were able to read user_info.txt - a user is logged in and we echo the information.
	*/

	$userInfoFile = fopen("user_info.txt", "r") or die("no user");
	echo fread($userInfoFile,filesize("user_info.txt"));


?>