var word = "";
var guessedLetters = "";

function updateGuessedLetters( letter ) {
    guessedLetters += letter;
    $.cookie("guessedLetters", guessedLetters, { expires : 365 } );
}

function loadGame() {
    var req = new XMLHttpRequest();
    var url = "hangmanController?";

    if( $.cookie("word") == undefined ) {
        url += "new_game";
        req.onreadystatechange = function() {
            if ( req.readyState == 4 && req.status == 200 ) {
                word = req.responseText.trim().toUpperCase();
                guessedLetters = "";
                $.cookie("word", word, { expires: 365 });
                $.cookie("guessedLetters", guessedLetters, { expires: 365 });
                updateGame();
            }
        }
    } else {
        word = $.cookie("word");
        guessedLetters = $.cookie("guessedLetters");
        url += "game_started";//&word=" + word + "&guessedLetters=" + guessedLetters;
        var guessLetters = $(".guessLetter");

        for ( var i = 0; i < guessLetters.length; i++ ) {
            for ( var j = 0; j < guessedLetters.length; j++ ) {
                if ( $(guessLetters[i]).html() == guessedLetters[j] ) {
                    guess ( guessedLetters[j], guessLetters[i] );
                }
            }
        }
        
        updateGame();
    }

    req.open("GET", url, true );
    req.send();

    // fade out intro and fade in game
    $("#intro").fadeOut(1000, function (){
        $("#game").css("display", "block");
        $("#game").fadeTo(1000, 1);
    });
}

function guess( letter, obj ) {
    for ( var i = 0; i < guessedLetters.length; i++ ) {
        if ( guessedLetters[i] == letter ) {
            $( obj ).fadeTo( 1000, 0.3 );
            return;
        }
    }

    guessedLetters += letter.toUpperCase();
    $( obj ).fadeTo( 1000, 0.3 );
    updateGame();
}

function updateGame() {
    var htmlString = "";
    var wrong = 0;
    $.cookie("guessedLetters",guessedLetters, { expires : 365 } );

    for ( var i = 0; i < word.length; i++ ) {
        currentValue = " ";
        for( var j = 0; j < guessedLetters.length; j++ ) {
            if ( guessedLetters[j] == word[i] ) {
               currentValue = word[i]; 
               break;
            }

            if ( j == guessedLetters.length - 1 ) {
                wrong++;
            }
        }

        htmlString += "<u>" + currentValue +"</u>&nbsp;";
    }

    $("#guessWord").html( htmlString );

    updateHangman( wrong );
}

function updateHangman( wrong ) {

}
