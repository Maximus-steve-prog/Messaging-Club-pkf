$(document).ready(function(){
    //  All variables of the forme
    let navMenu = $("#nav-menu"),
        navToggle =$("#nav__toggle"),
        search = $("#search"),
        searchBtn = $("#search-btn"),
        searchClose =$("#search-close"),
        sign_in_btn =$("#sign-in-btn"),
        sign_up_btn =$("#sign-up-btn"),
        conatiner = $(".security"),
        btnshowlogin =$("#nav__login"),
        chat_sidebar_profile_toggle =$(".chat-sidebar-profile-toggle"),
        forgotpassword =$(".forgotpassword");




    show_toggle_profile = true;
    chat_sidebar_profile_toggle.click(function(e){
        e.preventDefault();
        switch(show_toggle_profile){
            case true:
                $(".chat-sidebar-profile").addClass("active");
                show_toggle_profile = false;
                break;
            case false:
                $(".chat-sidebar-profile").removeClass("active");
                show_toggle_profile =true
                break;
        }

    });

    var bool = true;

    btnshowlogin.click(function(){
        switch(bool)
        {
            case bool=true :
                conatiner.addClass("show-form");
                conatiner.removeClass("sign-up-mode");

                bool=false;
                break;
            case bool=false:
                conatiner.removeClass("show-form");
                // conatiner.addClass("sign-up-mode");
                bool=true;
                break;
        }
    });

    navToggle.click(function(){
        switch(bool)
        {
            case bool=true :
                navMenu.addClass('show-menu');
                $("#ri__menu").removeClass('ri-menu-line');
                $("#ri__menu").addClass('ri-close-line');
                // conatiner.removeClass("sign-up-mode");
                bool=false;
                break;
            case bool=false:
                navMenu.addClass('show-menu');
                navMenu.removeClass('show-menu');
                $("#ri__menu").addClass('ri-menu-line');
                $("#ri__menu").removeClass('ri-close-line');
                // conatiner.addClass("sign-up-mode");
                bool=true;
                break;
        }
    });

    searchBtn.click(function(){
        search.addClass('show-search');
    });

    searchClose.click(function(){
        search.removeClass('show-search');
    });


    //   Action when btn sign in our sign-up is clicked
    sign_up_btn.click(function(){
        conatiner.addClass("sign-up-mode");
        conatiner.removeClass("forgot-password-mode");
    });

    forgotpassword.click(function(){
        conatiner.addClass("forgot-password-mode");
    });


    let backtologin = $("#backtologin"),
        backtoreg =$(".backtoreg");
    backtologin.click(function(){
        conatiner.removeClass("forgot-password-mode");
    });

    backtoreg.click(function(){
        conatiner.removeClass("sign-up-profile");
    });

    sign_in_btn.click(function(){
        conatiner.removeClass("sign-up-mode");
        conatiner.removeClass("sign-up-profile");
    });

});


