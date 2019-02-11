/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSearchResult(resultDataString) {
	let url="";
	for(let i=0;i<resultDataString.length;i++){
		url+="&"+resultDataString[i]["name"]+"="+resultDataString[i]["num"];
	}
	
    window.location.replace("checkout.html?email="+resultDataString[0]["username"]+url);
}
function getParameterByName(target) {

    let url = window.location.href;

    target = target.replace(/[\[\]]/g, "\\$&");


    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';


    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleResult(resultData) {


    console.log("handleResult: populating cart form from resultData");


    let movieTableBodyElement = jQuery("#cart");
    let count=0;

    for (let i = 0; i < resultData.length; i++) {
    	if(resultData[i]["id"]!=(null) && resultData[i]["number"]!=(0)){
	    	let rowHTML="<br><label><h3>"+resultData[i]["title"]+"</h3>    Quantity: </label>";
	    	rowHTML+="<input type=\"number\" min=0 name=\""+resultData[i]["id"]+"\" value="+resultData[i]["number"]+">";
	    	movieTableBodyElement.append(rowHTML);
	    	count+=1;
    	}


    }
    if(count>0){
    	movieTableBodyElement.append("<br><br><br><input type=\"submit\" value=\"Checkout\">");
    }
    else{
    	movieTableBodyElement.append("<br><br><br><p>Nothing in cart</p><a href=\"index.html\"> back to index</a>");
    }
}
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();
    $.post(
        "api/checkout",
        // Serialize the login form to the data sent by POST request
        $("#cart").serialize(),
        (resultDataString) => handleSearchResult(resultDataString)
    );
}
$("#cart").submit((event) => submitLoginForm(event));
jQuery.ajax({
    dataType: "json",  
    method: "GET",
    url: "api/cart", 
    success: (resultData) => handleResult(resultData)
});
