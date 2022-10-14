const coap       = require('coap')
     ,requestURI = 'coap://localhost/'
     ,url        = require('url').parse(requestURI + 'id/1/')
     ,req        = coap.request(url)
     ,bl         = require('bl');

req.setHeader("Accept", "application/json");
req.on('response', function(res) {
  res.pipe(bl(function(err, data) {
    var json = JSON.parse(data);
    console.log(json);
  }));

});
req.end();


