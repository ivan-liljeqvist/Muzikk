
<html>

<head>

<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>

<script>


function getHashParams() {

    var hashParams = {};
    var e,
        a = /\+/g,  // Regex for replacing addition symbol with a space
        r = /([^&;=]+)=?([^&;]*)/g,
        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
        q = window.location.hash.substring(1);

    while (e = r.exec(q))
       hashParams[d(e[1])] = d(e[2]);

    return hashParams;
}

function getUserData(accessToken) {
        return $.ajax({
            url: 'https://api.spotify.com/v1/me',
            headers: {
               'Authorization': 'Bearer ' + accessToken
            }
        });
}
</script>

</head>

<body>



<?php 
//if the user_id and email is not set - fetch and send to this page again
if(!isset($_GET['user_id']) and !isset($_GET['email'])){


?>

	<script type="text/javascript">
		getUserData(getHashParams()["access_token"])
		                .then(function(response) {
		                    location.replace("http://simkoll.com/muzikk/result.php?user_id="+response["id"]+"&email="+response["email"]);
		                });
	</script>

<?php
}
//if email and user id are set - save in a file
else{
	unlink('user_info.txt');

	$userInfoFile = fopen("user_info.txt", "w") or die("can't open file");
	$txt = $_GET['user_id']." ".$_GET['email'];
	fwrite($userInfoFile, $txt) or die("can't write file");;
	fclose($userInfoFile);

	echo "GO BACK TO MUZIKK";
}
?>




</body>

</html>