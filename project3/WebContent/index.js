/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleMainPage(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle session response");
    console.log(resultDataJson);
    console.log(resultDataJson["sessionID"]);

}



$.ajax({
    type: "POST",
    url: "api/index",
    success: (resultDataString) => handleMainPage(resultDataString)
});

