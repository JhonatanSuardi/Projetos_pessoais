import socket
HOST = ''              # Endereco IP do Servidor 2
PORT = 5002            # Porta que o Servidor 2 esta

#Criando o servidor t2
t2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
orig = (HOST, PORT) #Definindo a origem(servidor 1)
t2.bind(orig) #Vinculando o host com a porta do servidor
t2.listen(1) #Aguardando a conexão (limitado a 1)
while True:

    #Aceitando a conexão, retornando o objeto de socket do cliente
    con, cliente = t2.accept()
    print ('Concetado por', cliente)

    msg=con.recv(1024) #Recebendo a mensagem do cliente
    smsg=msg.decode() #Decodificando a mensagem recebida
    sni,sc,sd = smsg.split(";") #Separando a mensagem a partir do ';'
    ni=int(sni)
    c=int(sc)
    d=int(sd)

    for i in range (ni):
      x1 = c + d #fazendo a soma dos inteiros

      # reserva
      HOSTS = '127.0.0.1'     # Endereco IP do Servidor controlador
      PORTS = 5004            # Porta que o Servidor controlador esta

      #Criando conexão de socket com o controlador
      controlador = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS) #Definindo o destindo Controlador
      controlador.connect(dests) #Conectando com o o destino Controlador
      
      #Enviando o comando ao controlador
      controlador.send("reserva;T2".encode())
      resp=controlador.recv(1024) #Recebendo resposta do controlador
      if (resp.decode()=="reservado"): #Verfica reserva da região crítica
          print("entra na região critica\n")
      controlador.close() #Fechando conexão com o controlador

      # ----- Região Critica - INICIO
      HOSTS = '127.0.0.1'     # Endereco IP do Servidor Storage
      PORTS = 5003            # Porta que o Servidor Storage esta

      #Criando conexão de socket com o storage
      storage = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS)
      storage.connect(dests)

      grava="grava"+";"+str(x1) #Criando comando para o Storage
      storage.send(grava.encode()) #Enviando o comando ao Storage
      storage.close() #Fechando conexão com o Storage

      #Criando conexão de socket com o storage
      storage = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS)
      storage.connect(dests)

      le="le"+";"+str(0) #Criando comando para o Storage
      storage.send(le.encode()) #Enviando o comando ao Storage
      lx2=storage.recv(1024) #Recebendo a resposta do Storage
      storage.close() #Fechando a conxão com o Storage
      # ------Região Critica - FIM

      # libera
      HOSTS = '127.0.0.1'     # Endereco IP do Servidor controlador
      PORTS = 5004            # Porta que o Servidor controlador esta
      
      #Criando conexão de socket com o controlador
      controlador = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS)
      controlador.connect(dests)

      controlador.send("libera;T2".encode()) #Liberando o controlador
      resp=controlador.recv(1024) #Recebendo resposta do controlador
      if (resp.decode()=="liberado"): #Verifica a liberação 
        print("sai da região critica\n")
      controlador.close() #Fechando conexão com controlador

      x2=int(lx2.decode()) #Decodificando da mensagem recebida do Storage
      if (x1 != x2): #Verificando erro de leitura e gravação 
        print("erro ",x1)
    resp=str(x1)
    con.send(resp.encode()) #Enviando x1 para main
    con.close() #Fechando a conexão com o main
