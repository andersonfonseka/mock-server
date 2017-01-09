# mock-server

- < 20 segundos
- Sem codifica��o
- Quando precisar emular um servidor disponibilizando servi�os REST entregando JSON.

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

- <b>Router name</b> - quando necessitar criar um endere�o diferente, por exemplo: http://locahost:8080/meuservidor
- <b>Port number</b> - informar o n�mero de porta para a inicializa��o do servidor.
- <b>Json path</b> - o caminho do arquivo JSON.
- <b>Custom id</b> - permite definir um identificador para a busca da resposta JSON (padr�o id).

Chamando servidor via Postman

![alt tag](https://github.com/andersonfonseka/jsonfakeserver/blob/master/images/postman.png)

Com jsonfakeserver � poss�vel:

- � possivel executar o CRUD por meio dos m�todos GET, POST, PUT, DELETE.
- Adicionar outros tipos n�o previstos no json inicial. 

![alt tag](https://github.com/andersonfonseka/jsonfakeserver/blob/master/images/novojson.png)

