/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
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

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating star info from resultData");


    let starInfoElement = jQuery("#star_info");


    starInfoElement.append("<p>Star Name: " + resultData[0]["starName"] + "</p>" +
        "<p>Date Of Birth: " + resultData[0]["starDob"] + "</p>");

    console.log("handleResult: populating movie table from resultData");


    let movieTableBodyElement = jQuery("#movie_table_body");


    for (let i = 0; i < Math.min(10, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>"+'<a href="single-movie.html?id=' + resultData[i]["movieId"] + '">'
        + resultData[i]["movieTitle"] + 
        '</a>'; + "</th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "</tr>";


        movieTableBodyElement.append(rowHTML);
    }
}


let starId = getParameterByName('id');


jQuery.ajax({
    dataType: "json",  
    method: "GET",
    url: "api/single-star?id=" + starId, 
    success: (resultData) => handleResult(resultData)
});