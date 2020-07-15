import socket
import _thread

global x
x=0 #Valor armazenado

def conectado(con, cliente):
    global x 
    print ('Concetado por', cliente)
    msg=con.recv(1024) #Recebendo mensagem dos servidores
    smsg=msg.decode() #Decodificando o comando
    cmd,sx = smsg.split(";") #Separando o comando do valor
    
    if (cmd=="grava"): #"Gravando" o valor 
       print(cmd," ",sx)
       x=int(sx)
    elif (cmd=="le"): #"Lendo" o valor
       print(cmd," ",str(x))
       resp=str(x)
       con.send(resp.encode()) #Envia o valor armazenado no storage
    else:
    	print("ERRO\n")
    con.close() #Fecha conexão com o cliente conectado 
    
HOST = ''              # Endereco IP do Servidor Storage
PORT = 5003            # Porta que o Servidor Storage esta

#Criando o servidor storage 
storage = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
orig = (HOST, PORT)
storage.bind(orig)
storage.listen(128)
while True:
    #Aceita a conexão, retornando o objeto de socket do cliente
    con, cliente = storage.accept()
    #Criando uma thread para realizar as tarefas
    _thread.start_new_thread(conectado, tuple([con, cliente]))
    
