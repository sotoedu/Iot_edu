var express = require('express')
   ,http = require('http')
   ,bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

var server = app.listen(3000, function(){ 
   var host = server.address().address; 
   var port = server.address().port; 
   console.log('ExpressJS example at http://localhost:' + port);
});

var items = [];

// All list items
app.get('/products', function (req, res){
  res.send(items);
});

// Add item
app.post("/products", function( req, res){
    var name = req.body.name;
    var price = req.body.price;
    var item = {
      name: name,
      price: price
    };

  items.push(item);

  res.send({
    message: '데이터를 추가했다.',
    data: item
  });
});

// item list
app.get('/products/:id', function (req, res){
  var id = Number(req.params.id);

  if(isNaN(id)){
    res.send({
      error: '숫자를 입력하시오!'
    });
  }else if(items[id]){
    res.send(items[id]);
    }else{
      res.send({
        error: '존재하지 않는 데이터입니다.!'
      });
    }
});

// Update item
app.put('/products/:id', function (req, res){
  var id = Number(req.params.id);
  var name = req.body.name;
  var price = req.body.price;

  if(items[id]){
    if(name){ items[id].name = name;}
      if(price){ items[id].price = price;}

        res.send({
          message:'데이터를 수정했다.',
          data: items[id]
        });
  }else {
    res.send({
      error: '존재하지 않는다.'
    });
  }
});

// Delete item
app.delete('/products/:id', function (req, res){
  var id = Number(req.params.id);

  if(isNaN(id)){
    res.send({
      error:'숫자를입력'
    });
  }else if(items[id]){
    items.splice(id, 1);
    
    res.send({
          message:'데이터를 삭제했다.',
          id: id,
          data: items[id]
        });
  }else{
    res.send({
      error: '존재하지 않는 데이터!'
    });
  }
});


// Dose not exist url and error
app.use(function( req, res){
    res.writeHead(404,{"Content-Type" : "text/plain"});
    res.end('404 ERROR');
});


