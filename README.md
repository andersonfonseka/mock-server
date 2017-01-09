# mock-server

- < 20 segundos
- Sem codificação
- Quando precisar emular um servidor disponibilizando serviços REST entregando JSON.

- Criar um arquivo .json:

```json
{
  "posts": [
    {
      "id": 1,
      "title": "jsonfakeserver",
      "author": "andersonfonseka"
    }
   ]
}
```

- Baixe o arquivo <b>jsonfakeserver.zip</b> e descompacte.
- Execute java -jar jsonfakeserver-0.0.1-SNAPSHOT.jar 

![alt tag](https://github.com/andersonfonseka/jsonfakeserver/blob/master/images/mainscreen.png)

- <b>Router name</b> - quando necessitar criar um endereço diferente, por exemplo: http://locahost:8080/meuservidor
- <b>Port number</b> - informar o número de porta para a inicialização do servidor.
- <b>Json path</b> - o caminho do arquivo JSON.
- <b>Custom id</b> - permite definir um identificador para a busca da resposta JSON (padrão id).

Chamando servidor via Postman

![alt tag](https://github.com/andersonfonseka/jsonfakeserver/blob/master/images/postman.png)

Com jsonfakeserver é possível:

- É possivel executar o CRUD por meio dos métodos GET, POST, PUT, DELETE.
- Adicionar outros tipos não previstos no json inicial. 

![alt tag](https://github.com/andersonfonseka/jsonfakeserver/blob/master/images/novojson.png)

