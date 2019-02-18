
/**
 * Handle the items in item list 
 * @param resultDataString jsonObject, needs to be parsed to html 
 */
function handleCartArray(resultDataString) {
//    console.log(typeof resultDataString[0]);
//    console.log(resultDataString);
//    let output = resultDataString[0];
//    
//    // change it to html list
//    let tb = jQuery("#item_list_body");
//    for(let i = 0; i < output.length; i++) {
//        // each item will be in a bullet point
//    	let rowHTML=""
//        let tableName = "<th>"+output[i]["name"]+"</th>";
//        let col="<th>";
//        let ty="<th>";
//        
//        for(let z=0;z<output[i]["column"].length;z++){
//        	col+="<p>" + output[i]["column"][z]["cname"] + "</p>";
//        	ty+="<p>" + output[i]["column"][z]["ctype"] + "</p>";
//        }
//        col+="</th>";
//        ty+="</th>";
//        rowHTML += "<div><p>--------------------------</p></div>"
//        rowHTML = "<tr>"+tableName+col+ty+"</tr>";
//        rowHTML += "<div><p>--------------------------</p></div>"
//        tb.append(rowHTML);
//        
//    }
//    let result = jQuery("#resul-message");
    if(!resultDataString[0][0]["error-message"]==""){
    	alert(resultDataString[0][0]["error-message"])
    	}
}
function handleCartArray1(resultDataString){
    console.log(typeof resultDataString[0]);
    console.log(resultDataString);
    let output = resultDataString[0];
    
    // change it to html list
    let tb = jQuery("#item_list_body");
    for(let i = 0; i < output.length; i++) {
        // each item will be in a bullet point
    	let rowHTML=""
        let tableName = "<th>"+output[i]["name"]+"</th>";
        let col="<th>";
        let ty="<th>";
        
        for(let z=0;z<output[i]["column"].length;z++){
        	col+="<p>" + output[i]["column"][z]["cname"] + "</p>";
        	ty+="<p>" + output[i]["column"][z]["ctype"] + "</p>";
        }
        col+="</th>";
        ty+="</th>";
        rowHTML += "<div><p>--------------------------</p></div>"
        rowHTML = "<tr>"+tableName+col+ty+"</tr>";
        rowHTML += "<div><p>--------------------------</p></div>"
        tb.append(rowHTML);
        
    }
}

/**
 * Submit form content with POST method
 * @param cartEvent
 */
function handleCartInfo(cartEvent) {
    console.log("submit add_star form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.get(
        "api/makechange",
        // Serialize the cart form to the data sent by POST request
        $("#add_star").serialize(),
        (resultDataString) => handleCartArray(resultDataString)
    );
}

$.ajax({
//	dataType: "json",
    type: "POST",
    url: "api/makechange",
    success: (resultDataString) => handleCartArray1(resultDataString)
});

// Bind the submit action of the form to a event handler function
$("#add_star").submit((event) => handleCartInfo(event));


//***************************movie********************************

function handle_movie_api(resultDataString) {
//    let result = jQuery("#resul-message");
    if(!resultDataString[0][0]["error-message"]==""){
    	alert(resultDataString[0][0]["error-message"])
    }
}


function add_movieInfo(cartEvent) {
    console.log("submit add_movie form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.get(
        "api/add_movie",
        // Serialize the cart form to the data sent by POST request
        $("#add_movie").serialize(),
        (resultDataString) => handle_movie_api(resultDataString)
    );
}

$("#add_movie").submit((event) => add_movieInfo(event));
