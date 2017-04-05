var line = document.getElementById("line");
var td = line.getElementsByTagName("td");
var btn = document.getElementById("loginButton");
for(var i = 0;i < td.length;i++){
	td[i].onclick = select;
}
function select(element){
	for(var j = 0;j < td.length;j++){
		td[j].className = '';
	}
	this.className = "selected";
}
function ajax(url, fnSucc, fnFaild){
	var oAjax = null;
	if(window.XMLHttpRequest){
		oAjax = new XMLHttpRequest();
	}else{
		oAjax = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	oAjax.open('GET', url, true);
	
	oAjax.send();
	
	oAjax.onreadystatechange = function(){
		if(oAjax.readystate == 4){
			if(oAjax.status == 200){
				fnSucc(oAjax.responseText)
			}else{
				if(fnFaild){
					fnFaild();
				}
			}
		}
	}
}
btn.onclick = function(){
	ajax('abc.txt', function(str){})
}
