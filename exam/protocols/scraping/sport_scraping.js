var request = require("request");
var cheerio = require('cheerio');
var iconv = require('iconv-lite');
var fs = require('fs');

var array = [];
var data = [];


var requestOptions = {
  method: "GET"
  , uri: "http://www.fifa.com/fifa-world-ranking/index.html"
  , headers: { "User-Agent": "Mozilla/5.0" }
  , encoding: null
};

request(requestOptions, function (error, response, body) {
  if (!error && response.statusCode == 200) {
    var $ = cheerio.load(body);

    $('.tbl-countrycode').each(function (j) {
      countrycode = $(this).text();
      array.push(countrycode);
      // console.log("countrycode: " + countrycode);
      // console.log(array.length);
    });

    $('.tbl-points').each(function (j) {
      points = $(this).text();
      array.push(points);
      // console.log("points: " + points);
      // console.log(array.length);
    });

    console.log("Top 10 - Men's");
    console.log($('.ranking-dates').text());

    for (var lp = 1; lp < 11; lp++) {

    var metadata = {
		countrycode: array[lp],
		points: array[lp+11],		
	};

    
      data.push(metadata);
    }
    console.log(data);

  } // end of if
});  // end of request
