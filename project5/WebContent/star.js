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


    let starTableBodyElement = jQuery("#star_table_body");


    for (let i = 0; i < Math.min(10, resultData.length); i++) {

        
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +

            '<a href="single-star.html?id=' + resultData[i]['starId'] + '">'
            + resultData[i]["starName"] +     
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["starDob"] + "</th>";
        rowHTML += "</tr>";


        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */


jQuery.ajax({
    dataType: "json", 
    method: "GET", 
    url: "api/stars", 
    success: (resultData) => handleStarResult(resultData)
});