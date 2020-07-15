import socket
HOST1 = '127.0.0.1'     # Endereco IP do Servidor 1
PORT1 = 5001            # Porta que o Servidor 1 esta
HOST2 = '127.0.0.1'     # Endereco IP do Servidor 2
PORT2 = 5002            # Porta que o Servidor 2 esta

t1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM) #criando o Socket T1
dest1 = (HOST1, PORT1) #Definindo o destino t1
t1.connect(dest1) #conectando ao destino t1

t2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM) #criando o Socket T2
dest2 = (HOST2, PORT2) #definindo o destino t2
t2.connect(dest2) #conectando ao destino t2

#-----------Definindo a mensagem do t1
ni=1000
a=1
b=2
#Criando a mensagem a ser enviada ao destino t1
msg1=str(ni)+";"+str(a)+";"+str(b)+"\n" 
t1.send(msg1.encode()) #Enviando a mensagem ao t1
#Fim da definição da mensagem do t1

#-----------Definindo a mensagem do t2
c=3
d=4 
#Criando a mensagem a ser enviada ao destino t2
msg2=str(ni)+";"+str(c)+";"+str(d)+"\n"
#Fim da definição da mensagem do t1

t2.send(msg2.encode()) #Enviando a mensagem ao t2
resp1=t1.recv(1024) #Recebendo a resposta do t1
print("t1: ",int(resp1)) #mostrando a resposta do t1
resp2=t2.recv(1024) #Recebendo a resposta do t2
print("t2: ",int(resp2)) #mostrando a resposta do t1
t1.close() #Fechando conexão com t1
t2.close() #Fechando conexão com t2
print ("fim") #mostrando fim :D
