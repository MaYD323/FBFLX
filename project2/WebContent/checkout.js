/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */

function getParameterByName(target) {

    let url = window.location.href;

    target = target.replace(/[\[\]]/g, "\\$&");


    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';


    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle complete check out response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
    if (resultDataJson["status"] === "success") {
        window.location.replace("index.html");
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#checkout_error_message").text(resultDataJson["message"]);
    }
}



function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");

    formSubmitEvent.preventDefault();
    
    $.post(
        "api/completecheckout",
        // Serialize the login form to the data sent by POST request
        $("#checkout_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    );
}

// Bind the submit action of the form to a handler function
$("#checkout_form").submit((event) => submitLoginForm(event));