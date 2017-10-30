$(function(){
    function getMessages(){
    	$(".chat").load("/txt/livechat.txt");
    }
    
	setInterval(function(){
		getMessages();
	},500);
});