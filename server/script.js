
 /*
    Author - Ivan Liljeqvist 
    Version - 2015-05-10

    This script runs on the index page Muzikk sends you to when you
    want to log in fromt the Java client.

    We have a login button that starts the Spotify login window.
    We trigger a click on that button.
 */

(function() {

        /*
            ID for Muzikk from Spotify
        */
        var CLIENT_ID = 'e45112497ae04346b38d3e6e2f73af23';
        /*
            Send the user here when the login has succeeded-
        */
        var REDIRECT_URI = 'http://simkoll.com/muzikk/result.php';

        /*
            Generates the URL for Spotify login window.
            Pass in scopes - the 'permissions'
        */

        function getLoginURL(scopes) {
            return 'https://accounts.spotify.com/authorize?client_id=' + CLIENT_ID +
              '&redirect_uri=' + encodeURIComponent(REDIRECT_URI) +
              '&scope=' + encodeURIComponent(scopes.join(' ')) +
              '&response_type=token';
        }
        
        /*
            Generate URL for getting the user's mail.
        */

        var url = getLoginURL(['user-read-email']);


        
        /*
            Here we create a login button that will redirect the user 
            to Spotify login page when clicked.
        */
    
            
        var loginButton = document.getElementById('btn-login');
    
        loginButton.addEventListener('click', function() {

            location.replace(url);
        });

        /*
            Ideally, we dont want the user to click the button in order to get to the login screen.
            We want it happen automatically. Therefore we trigger the click-event on the button.

            However, this might not work in some browsers. Then the user has to click on the login button.
        */

        $( "#btn-login" ).trigger( "click" );


})();
    
        

