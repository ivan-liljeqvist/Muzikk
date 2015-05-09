<?php

	$userInfoFile = fopen("user_info.txt", "r") or die("no user");
	echo fread($userInfoFile,filesize("user_info.txt"));


?>