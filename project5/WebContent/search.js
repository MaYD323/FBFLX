/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle search response");
    window.location.replace("movies.html?title="+resultDataJson["title"]+"&year="+resultDataJson["year"]+"&director="+resultDataJson["director"]+"&star="+resultDataJson["star"]+"&sorting=rating&npp=20&page=1&genre=&st=");
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/search",
        // Serialize the login form to the data sent by POST request
        $("#search_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString)
    );
}

// Bind the submit action of the form to a handler function
$("#search_form").submit((event) => submitSearchForm(event));

