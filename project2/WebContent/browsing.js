/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
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
function handleStarResult(resultData) {
    console.log("handleBrowsingResult: populating star table from resultData");

    
    let genreElement = jQuery("#genre_list");
    let rowHTML = " | ";
    for (let i = 0; i < resultData.length; i++) {
        rowHTML += '<a href="movies.html?title=&year=&director=&star=&sorting=rating&npp=20&page=1&genre='+resultData[i]["name"]+"&st="+'">'+resultData[i]["name"]+"</a>";
        rowHTML+=" | ";
        
    }
    genreElement.append(rowHTML);
    let titleElement = jQuery("#title_list");
    let list = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','1', '2', '3', '4', '5', '6', '7', '8', '9', '0'];
    let ss = " | ";
    for(let i=0;i<list.length;i++){
    	ss += '<a href="movies.html?title=&year=&director=&star=&sorting=rating&npp=20&page=1&genre=&st='+list[i]+'">'+list[i]+"</a>";
    	ss += " | ";
    }
	titleElement.append(ss);
    }




jQuery.ajax({
    dataType: "json",  
    method: "GET",
    url: "api/browsing", 
    success: (resultData) => handleStarResult(resultData) 
});