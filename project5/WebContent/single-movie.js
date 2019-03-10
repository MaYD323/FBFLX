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

    let movieInfoElement = jQuery("#movie_info");


    let inf = "<p>Movie Title: " + resultData[0]["movieTitle"] + "</p>"+"<p>Rating: " + resultData[0]["rating"] + "</p>"+"<p>Director: " + resultData[0]["movieDirector"] + "</p>"+"<p>Year: " + resultData[0]["movieYear"] + "</p>"+"<p>ID: " + resultData[0]["movieId"] + "</p>";
    movieInfoElement.append(inf);
    let starTableBodyElement = jQuery("#star_table_body");
	let rowHTML="<tr>";
	rowHTML+= "<th>";
    for(let i=0;i < resultData.length;i++){

    	rowHTML+="<span>"+'<a href="single-star.html?id=' + resultData[i]["starId"]+"&title=" + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+"&npp="+resultData[0]["npp"]+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"] + '">'
        + resultData[i]["starName"] + 
        '</a>';+"</span>";
        if(i != resultData.length - 1) {
        	rowHTML += ", ";
        } 
    }
    starTableBodyElement.append(rowHTML);
    rowHTML+= "</th>";
    rowHTML+="</tr>";
    let genreTableBodyElement = jQuery("#genre_table_body");
	rowHTML="<tr>";
	rowHTML+="<th>";
    for(let i=0;i < resultData[0]["genres"].length;i++){

    	rowHTML+="<span>"+ resultData[0]["genres"][i]["name"] + "</span>";

    }
    rowHTML += "</th>";
    rowHTML+="</tr>";
    genreTableBodyElement.append(rowHTML);
    $('#Back_Button').click(function() {
    	  window.location="movies.html?title=" + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+"&npp="+resultData[0]["npp"]+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"];
    	});
    $('#add_form').append("<input type=\"hidden\" name=\"item\" value="+resultData[0]["movieId"]+"><input type=\"submit\" value=\"add\">");

}


let movieId = getParameterByName('id');
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let star = getParameterByName('star');
let sorting = getParameterByName('sorting');
let npp = getParameterByName('npp');
let page = getParameterByName('page');
let genre = getParameterByName('genre');
let st = getParameterByName('st');


jQuery.ajax({
    dataType: "json",  
    method: "GET",
    url: "api/single-movie?id=" + movieId+"&title=" + title+"&year="+year+"&director="+director+"&star="+star+"&sorting="+sorting+"&npp="+npp+"&page="+page+"&genre="+genre+"&st="+st, 
    success: (resultData) => handleResult(resultData) 
});

