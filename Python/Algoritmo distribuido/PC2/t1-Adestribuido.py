import time
import socket
import threading
import _thread

m = threading.Lock() #Mutex
global uso 
uso="ninguem" #Identificador de uso do controlador

def conectado(con, cliente):
    print ('Concetado por', cliente)
    msg=con.recv(1024) #Recebendo a mensagem
    smsg=msg.decode() #Decondificando a mensagem
    cmd,nome = smsg.split(";") #Dividindo a mensagem em comando e nome
    print("*** "+cmd+" "+nome+"\n")
    #-----------Verifica comando recebido - Inicio
    if (cmd=="reserva"):
       m.acquire() #Reserva da região crítica
       uso=nome #Definindo quem está usando a região crítica
       con.send("reservado".encode()) #Enviando a confirmação ao cliente atual
       print("reservado "+nome+"\n")
    elif (cmd=="libera"):
       m.release() #Libera a região crítica
       uso="ninguem" #Define que ninguém está usando a região crítica
       print("liberado "+nome+"\n")
       con.send("liberado".encode()) #Envia a confirmação ao cliente atual
    else:
       print("ERRO\n")
    con.close() #Fecha conexão com o cliente
    #-----------Verifica comando recebido - Fim

def controle(i,j):
    print("Controle") 
    HOST = ''              # Endereco IP do T1 controle
    PORT = 5004            # Porta que o T1 usa controle

    #Criando o servidor Controlador
    controlador = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    orig = (HOST, PORT)
    controlador.bind(orig)
    controlador.listen(128)
    
    while True:
      #Aceita a conexão, retornando o objeto de socket do cliente  
      con, cliente = controlador.accept()
      #Criando uma thread para realizar as tarefas
      _thread.start_new_thread(conectado, tuple([con, cliente]))
    

_thread.start_new_thread(controle,tuple([1, 2]))
HOST = ''              # Endereco IP do Servidor 1
PORT = 5001            # Porta que o Servidor 1 esta

#Criando o servidor t1
main = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
orig = (HOST, PORT) #Definindo a origem(servidor 1)
main.bind(orig) #Vinculando o host com a porta do servidor
main.listen(1) #Aguardando a conexão (limitado a 1)
while True:

    #Aceita a conexão, retornando o objeto de socket do cliente
    con, cliente = main.accept()
    print ('Conectado por', cliente)
    msg=con.recv(1024) #Recebendo a mensagem do cliente
    smsg=msg.decode() #Decodificando a mensagem recebida
    sni,sa,sb = smsg.split(";") #Separando a mensagem a partir do ';'
    ni=int(sni)
    a=int(sa)
    b=int(sb)

    for i in range (ni):
      x1 = a + b #fazendo a soma dos inteiros

      # reserva
      HOSTS = '127.0.0.1'     # Endereco IP do T2
      PORTS = 5005            # Porta que o T2 esta
      
      #Criando conexão de socket com o controlador
      controlador = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS)
      controlador.connect(dests)

      #Enviando o comando ao controlador
      controlador.send("reserva;T1".encode())
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

      #Criando conexão de socket com o Storage
      storage = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS)
      storage.connect(dests)
      
      le="le"+";"+str(0) #Criando comando para o Storage
      storage.send(le.encode()) #Enviando o comando ao Storage
      lx2=storage.recv(1024) #Recebendo a resposta do Storage
      storage.close() #Fechando a conxão com o Storage
      # ------Região Critica - FIM

      # libera
      HOSTS = '127.0.0.1'     # Endereco IP do T2
      PORTS = 5005            # Porta que o T2
      
      #Criando conexão de socket com o controlador
      controlador = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      dests = (HOSTS, PORTS) 
      controlador.connect(dests)

      controlador.send("libera;T1".encode()) #Liberando o controlador
      resp=controlador.recv(1024) #Recebendo resposta do controlador
      if (resp.decode()=="liberado"): #Verifica a liberação 
        print("sai da região critica\n")
      controlador.close() #Fechando conexão com controlador

      x2=int(lx2.decode()) #Decode da mensagem recebida do Storage
      if (x1 != x2): #Verificando erro de leitura e gravação
        print("erro ",x1)
    resp=str(x1)
    con.send(resp.encode()) #Enviando x1 para main
    con.close() #Fechando a conexão com o main
