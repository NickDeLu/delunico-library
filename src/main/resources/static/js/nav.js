/**
 * creates a responsive nav bar with the click of a hamburger button
 */

function hamburger(){
	var navbar = document.getElementById("myTopnav");
	if (navbar.className === "topnav") {
    	navbar.className += " responsive";
  } else {
  	navbar.className = "topnav";
  }
}
function goBack() {
	  window.history.back();
	}