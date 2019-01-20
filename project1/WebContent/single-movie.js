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


    let starInfoElement = jQuery("#movie_info");


    let inf = "<p>Movie Title: " + resultData[0]["movieTitle"] + "</p>"+"<p>Rating: " + resultData[0]["rating"] + "</p>"+"<p>Director: " + resultData[0]["movieDirector"] + "</p>"+"<p>Year: " + resultData[0]["movieYear"] + "</p>"+"<p>ID: " + resultData[0]["movieId"] + "</p>";
    starInfoElement.append(inf);
    let starTableBodyElement = jQuery("#star_table_body");
    
    for(let i=0;i < resultData.length;i++){
    	let rowHTML="<tr>";
    	rowHTML+="<th>"+'<a href="single-star.html?id=' + resultData[i]["starId"] + '">'
        + resultData[i]["starName"] + 
        '</a>';+"</th>";
        rowHTML+="</tr>";
        starTableBodyElement.append(rowHTML);
    }
    let genreTableBodyElement = jQuery("#genre_table_body");
    
    for(let i=0;i < resultData[0]["genres"].length;i++){
    	let rowHTML="<tr>";
    	rowHTML+="<th>"+ resultData[0]["genres"][i]["name"] + "</th>";
        rowHTML+="</tr>";
        genreTableBodyElement.append(rowHTML);
    }
    
    



    
}



let starId = getParameterByName('id');


jQuery.ajax({
    dataType: "json",  
    method: "GET",
    url: "api/single-movie?id=" + starId, 
    success: (resultData) => handleResult(resultData) 
});