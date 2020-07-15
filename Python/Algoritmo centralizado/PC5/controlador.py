import time
import socket
import threading
import _thread

m = threading.Lock() #mutex
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
 

print("Controlador") 
HOST = ''              # Endereco IP do Controlador
PORT = 5004            # Porta que do Controlador

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
    
