$(function(){

    $('.add-team-button').click(function(){
        $('.add-team-form').css('display', 'flex')
    })

    $('.add-team-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-add-team-button').click(function() {
        var data = $('.add-team-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/teams/',
            data: data,
            success: function(response) {
                $('.add-team-form').css('display', 'none');
            }
        })
        return false;
    })

    $('.correct-team-button').click(function(){
        $('.correct-team-form').css('display', 'flex')
    })

    $('.correct-team-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-correct-team-button').click(function() {
        var data = $('.correct-team-form form').serialize();
        var dataArray = $('.correct-team-form form').serializeArray();
        var id =dataArray[0]['value'];
        $.ajax({
            method: "PATCH",
            url: '/teams/' + id,
            data: data,
            success: function(response) {
                $('.correct-team-form').css('display', 'none');
            }
        })
        return false;
    })

    $('.delete-team-button').click(function(){
        $('.delete-team-form').css('display', 'flex')
    })

    $('.delete-team-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-delete-team-button').click(function() {
        var data = $('.delete-team-form form').serialize();
        var dataArray = $('.delete-team-form form').serializeArray();
        var id =dataArray[0]['value'];
        $.ajax({
            method: "DELETE",
            url: '/teams/' + id,
            data: data,
            success: function(response) {
                $('.delete-team-form').css('display', 'none');
            }
        })
        return false;
    })

    $('.add-player-button').click(function(){
        $('.add-player-form').css('display', 'flex')
    })

    $('.add-player-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-add-player-button').click(function() {
        var data = $('.add-player-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/players/',
            data: data,
            success: function(response) {
                $('.add-player-form').css('display', 'none');
            }
        })
        return false;
    })

    $('.correct-player-button').click(function(){
        $('.correct-player-form').css('display', 'flex')
    })

    $('.correct-player-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-correct-player-button').click(function() {
        var data = $('.correct-player-form form').serialize();
        var dataArray = $('.correct-player-form form').serializeArray();
        var id =dataArray[0]['value'];
        $.ajax({
            method: "PATCH",
            url: '/players/' + id,
            data: data,
            success: function(response) {
                $('.correct-player-form').css('display', 'none');
            }
        })
        return false;
    })

    $('.delete-player-button').click(function(){
        $('.delete-player-form').css('display', 'flex')
    })

    $('.delete-player-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none')
        }
    })

    $('.save-delete-player-button').click(function() {
        var data = $('.delete-player-form form').serialize();
        var dataArray = $('.delete-player-form form').serializeArray();
        var id =dataArray[0]['value'];
        $.ajax({
            method: "DELETE",
            url: '/players/' + id,
            data: data,
            success: function(response) {
                $('.delete-player-form').css('display', 'none');
            }
        })
        return false;
    })

})