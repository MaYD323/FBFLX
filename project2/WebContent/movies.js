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
    console.log("handleStarResult: populating star table from resultData");

//    let num_per_page = jQuery("#num_per_page");
    if(resultData[0]["npp"]=='10'){
	    $('#num_per_page').append('<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp="+resultData[0]["npp"]+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + resultData[0]["npp"] + '</option>'+ 
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=20"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "20" + '</option>'+
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=50"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "50" + '</option>');
    }
    if(resultData[0]["npp"]=='20'){
	    $('#num_per_page').append('<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp="+resultData[0]["npp"]+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + resultData[0]["npp"] + '</option>'+ 
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=10"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "10" + '</option>'+
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=50"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "50" + '</option>');
    } 
    if(resultData[0]["npp"]=='50'){
	    $('#num_per_page').append('<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp="+resultData[0]["npp"]+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + resultData[0]["npp"] + '</option>'+ 
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=10"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "10" + '</option>'+
	    		'<option value="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp=20"+"&page="+resultData[0]["page"]+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "20" + '</option>');
    } 
    
    let movieTableBodyElement = jQuery("#movie_table_body");
    let ts = jQuery("#title_sorting");
    let rs = jQuery("#rating_sorting");
    ts.append('<a href="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting=title"+ "&npp="+resultData[0]["npp"]+"&page=1"+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "title" + '</a>');
    rs.append('<a href="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting=rating"+"&npp="+resultData[0]["npp"]+"&page=1"+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">' + "rating" + '</a>');
    let total_page = resultData[0]["totalPage"];
    let page = resultData[0]["page"];
    if(parseInt(page)==1){
    	$('#previous_button').append("previous");
    }
    else{
    	$('#previous_button').append('<a href="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp="+resultData[0]["npp"]+"&page="+(parseInt(resultData[0]["page"])-1)+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">'+"previous");
    }
    if(parseInt(total_page)==parseInt(page)){
    	$('#next_button').append("next");
    }
    else{
     	$('#next_button').append('<a href="movies.html?title=' + resultData[0]["urlTitle"]+"&year="+resultData[0]["urlYear"]+"&director="+resultData[0]["urlDirector"]+"&star="+resultData[0]["urlStar"]+"&sorting="+resultData[0]["sorting"]+ "&npp="+resultData[0]["npp"]+"&page="+(parseInt(resultData[0]["page"])+1)+"&genre="+resultData[0]["genreWord"]+"&st="+resultData[0]["st"]+'">'+"next");
    }
    for (let i = 0; i < resultData.length; i++) {


        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +

            '<a href="single-movie.html?id=' + resultData[i]['movieId'] + "&title=" + title+"&year="+year+"&director="+director+"&star="+star+"&sorting="+sorting+"&npp="+npp+"&page="+page+"&genre="+genre+"&st="+st+'">'
            + resultData[i]["movieTitle"] +     
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movieYear"]+"</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieRating"] + "</th>";
        rowHTML += "<th>"
        for(let z=0;z<resultData[i]["stars"].length;z++)
        {
        	rowHTML +='<a href="single-star.html?id=' + resultData[i]["stars"][z]["starId"] +"&title=" + title+"&year="+year+"&director="+director+"&star="+star+"&sorting="+sorting+"&npp="+npp+"&page="+page+"&genre="+genre+"&st="+st+ '">'
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
        	rowHTML += '<a href="movies.html?title=&year=&director=&star=&sorting=rating&npp=20&page=1&genre='+resultData[i]["genre"][x]["name"]+"&st="+'">'+resultData[i]["genre"][x]["name"]+"</a>";
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
    url: "api/movies?title=" + title+"&year="+year+"&director="+director+"&star="+star+"&sorting="+sorting+"&npp="+npp+"&page="+page+"&genre="+genre+"&st="+st, 
    success: (resultData) => handleStarResult(resultData) 
});