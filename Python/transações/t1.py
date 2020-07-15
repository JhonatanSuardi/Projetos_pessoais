import mysql.connector as mysql

global r
r = 0

def calculo(x,n):
  db=mysql.connect(host = "localhost",user = "root",  passwd = "zaq12wsx", db="DADOS") #Definindo os dados de acesso ao banco de dados

  cursor = db.cursor(buffered=True) #Cursor(Interage com o banco de dados)
  query = "INSERT INTO valores (id, valor) VALUES (%s, %s)" #Inserindo valores da tabela
  values = ("R1", 0) #Valores a ser inserido na tabela
  cursor.execute(query, values) #Executando o comando
  db.commit() #Salvando as alterações feitas no banco

  global r
  
  for i in range (x,x+n):
    print(i)
    cursor.execute("select valor from valores where id='R1'") #Consultando
    data = cursor.fetchone() #Retorna a linha do resultado
    r = int(data[0])
    r = r + i
    query="""UPDATE valores SET valor = %s WHERE id = %s""" #Atualizando o dado do banco
    cursor.execute(query,(r,"R1")) #Executando a query
    db.commit() #Salvando alterações
    for j in range (100):
      for k in range (10000):
          a = 1 

calculo(0,100)
print()
print(r)
