
    //window.open("http://www.w3schools.com");
(function() {


        var CLIENT_ID = 'e45112497ae04346b38d3e6e2f73af23';
        var REDIRECT_URI = 'http://simkoll.com/muzikk/result.php';

        function getLoginURL(scopes) {
            return 'https://accounts.spotify.com/authorize?client_id=' + CLIENT_ID +
              '&redirect_uri=' + encodeURIComponent(REDIRECT_URI) +
              '&scope=' + encodeURIComponent(scopes.join(' ')) +
              '&response_type=token';
        }
        
        var url = getLoginURL(['user-read-email']);

        
        var width = 450,
            height = 730,
            left = (screen.width / 2) - (width / 2),
            top = (screen.height / 2) - (height / 2);
    
            
        var loginButton = document.getElementById('btn-login');

    
        loginButton.addEventListener('click', function() {
            /*var w = window.open(url,
                            'Spotify',
                            'menubar=no,location=no,resizable=no,scrollbars=no,status=no, width=' + width + ', height=' + height + ', top=' + top + ', left=' + left
                           );*/

            location.replace(url);
        });

        $( "#btn-login" ).trigger( "click" );


})();
    
        

