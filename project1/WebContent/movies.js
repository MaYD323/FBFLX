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
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");


    let movieTableBodyElement = jQuery("#movie_table_body");


    for (let i = 0; i < resultData.length; i++) {


        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +

            '<a href="single-movie.html?id=' + resultData[i]['movieId'] + '">'
            + resultData[i]["movieTitle"] +     
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieRating"] + "</th>";
        rowHTML += "<th>"
        for(let z=0;z<resultData[i]["stars"].length;z++)
        {
        	rowHTML +='<a href="single-star.html?id=' + resultData[i]["stars"][z]["starId"] + '">'
            + resultData[i]["stars"][z]["name"] + 
            '</a>';
        	if(z!=resultData[i]["stars"].length-1)
        		{
        		rowHTML+=", ";
        		}
        }
        
        
        rowHTML += "</th>"
        rowHTML += "<th>"
        for(let x=0;x<resultData[i]["genre"].length;x++)
        {
        	rowHTML += resultData[i]["genre"][x]["name"];
        	if(x!=resultData[i]["genre"].length-1)
    		{
    		rowHTML+=", ";
    		}
        }
        rowHTML += "</th>"
        rowHTML += "</tr>";

        movieTableBodyElement.append(rowHTML);
    }
}




jQuery.ajax({
    dataType: "json", 
    method: "GET", 
    url: "api/movies", 
    success: (resultData) => handleStarResult(resultData)
});