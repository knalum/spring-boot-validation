$(document).ready(function(){
   console.log("Ready")


    checkLoginStatus();

    function userIsLoggedIn(username) {
        console.log(username)
        $("#logged-in #greeting").text("Hello "+username);
        $("#logged-in").show();
        $("#logged-out").hide();
    }

    function userIsNotLoggedIn(){
        $("#logged-in").hide();
    }

    $("#log-out-btn").click(function(){
        console.log("Logout");
        $.ajax({
            url:"/logout",
            method:"POST",
            success:function(data,res){
                console.log(res)
            },
            error: function (data, res) {
                console.log(data,res)
            }
        })
    })

    function checkLoginStatus(){
       $.ajax({
           url: "/user",
           success: function(data,res){
               console.log(data,res)
               userIsLoggedIn(data.userAuthentication.details.name);
           },
           error: function(data,res){
               console.log(data,res)
               userIsNotLoggedIn()
           }
       })
    }
});