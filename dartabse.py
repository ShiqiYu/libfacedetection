import sqlite3

conn = sqlite3.connect('test.db')
print ("Opened database successfully");

conn.execute('''CREATE TABLE Login
         (username varchar PRIMARY KEY     NOT NULL,
         password           TEXT    NOT NULL);''')
print ("Table created successfully");
conn.execute("INSERT INTO Login (userNAME,password) \
      VALUES ('admin', 'admin' )");
conn.execute("INSERT INTO Login (userNAME,password) \
      VALUES ('gla', 'gla' )");
conn.commit();
print("Data inserted successfully");

conn.close()
