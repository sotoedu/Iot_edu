const coap    = require('coap') 
    , server  = coap.createServer()
    
var accountObj = {
    name: "family",
    number: 10645,
    members: ["sun, star"],
    location: "Korea"
};    

server.on('request', function(req, res) {
      res.end(JSON.stringify(accountObj))
      console.log(' Have a nice day~~~')
})

server.listen(function() {
 console.log('server started')
})

