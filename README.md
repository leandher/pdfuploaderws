# PDFUploaderWS

Servidor REST que salva arquivos PDF e imprime uma hash formada pelo CPF que é enviada por parametro, ip de quem requisitou e pela data e hora no momento da requisição.

É composto por um método POST que recebe um FormData composto pelo arquivo e pelo CPF, retorna o status 500 caso haja erro no servidor, retorna 400 caso haja um erro no envio dos dados(quando arquivo não é PDF)

