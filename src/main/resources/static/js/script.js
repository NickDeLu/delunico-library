function verify() {

	var password = document.forms['form']['password'].value;
	var userName = document.forms['form']['userName'].value;
	if (password == null || password == "" || 
		userName == null || userName == "") {
		
		document.getElementById("error").innerHTML 
			= "User name and password are required";
		return false;
	}

	var checkboxes = document.getElementsByName("authorities");
    var okay = false;
    for(var i = 0; i < checkboxes.length; i++) {
        if(checkboxes[i].checked) {
            okay = true;
            break;
        }
		document.getElementById("error").innerHTML 
			= "You must select at least one role";
    } 
	return okay;
}

