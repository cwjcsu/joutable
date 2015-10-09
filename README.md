# An simple and pretty formatted Table in Java
You may want to print out some simple table (with title,headers) to the console with a readable format,then joutable is your best choise.

# Usages
## How to Use joutable ?
```
        DefaultTable dt = new DefaultTable();
        dt.setTitle("Aacademic Record Table");
        dt.setHeaders(new String[] { "Id", "Name", "Grade", "Remark" });
        dt.addRow(new Object[] { 1001, "Jack Bower", "A", "Good at fighting" });
        dt.addRow(new Object[] { 1002, "Vincent Willem  Van Gogh", "A+","Good at painting" });
        dt.addRow(new Object[] { 1003, "Jone Doe", "B", "Noop" });
        DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
        System.out.println(dtf.format(dt));
```
this will print a pretty formatted table :
```
=============== Aacademic Record Table ===============
Id     Name                      Grade  Remark          
-----  ------------------------  -----  ----------------  
1001   Jack Bower                A      Good at fighting
1002   Vincent Willem  Van Gogh  A+     Good at painting
1003   Jone Doe                  B      Noop   
```

You can customize the format: 
* Padding char of the title;
* Separating char between headers and body
* Set `sort=true` to sort rows by the first column,by default
* Set `minColumnWidth` to make column width to fill your need

## Examples

### A long title table

```
 Longongongongongongongongongongongongongongongongongongongongongongongongongongongongongongong title 
Id                        Name                      Node                      Status                  
------------------------  ------------------------  ------------------------  ------------------------  
id0                       Node0                     0sss                      Stopped                 
id1                       Node1                     1sss                      Stopped                 
id2                       Node2                     2sss                      Stopped    
```

### A long header table
```
========================= A title =========================
LLLLLLLLLLLLLLLLLLLLLLLLLLLlongHeader  Name   Node   Status 
-------------------------------------  -----  -----  -------  
id0                                    Node0  0sss   Stopped
id1                                    Node1  1sss   Stopped
id2                                    Node2  2sss   Stopped
```

### A table with column wrapped
```
======== Short wrapped column ========
Id     Name         Node         Status 
-----  -----------  -----------  -------  
id0    Node0        0sss         Stopped
id1    d2ps3oody01  1sss6500491  Stopped
                    34367211423         
                    0                   
id3    2h5uqrp3jrz  3sss9128827  Stopped
       ns6bsynzz45  32113350427         
       pu3pxysa3    3                   
id4    0n9412d76fn  4sss7805302  Stopped
       vd42a9sv3nt  45755729772         
       3tivlli8eh4  3                   
       3qb89a24  
```

### A row-sorted table
```
 A sorted table 
Id       Name   
-------  -------  
1        Node1  
2        Node2  
2        Node2X 
2        Node2Y 
3        Node3  
4        Node4  
```

### A table with customed indent
```
  *   A intent table 
  *  Id       Name   
  *  -------  -------  
  *  3        Node1  
  *  1        Node1  
  *  4        Node1  
  *  2        Node1  
```

### A table with arrow padded title
```
<<<<<<<<<<<<<<<<<<<<<<<< arrow padded title >>>>>>>>>>>>>>>>>>>>>>>>
Id     Name                         State                             
-----  ---------------------------  ----------------------------------  
1      LOOOOOOOOOOOOOOOOOOOOOOOOng  LOOOOOOOOOOOOOOOOOOOOOOOOng column
2      Node2                        Running                           
3      Node3                        Running                           
4      Node4                        Running       
```

### A table with custom cell format
```
====== A date cell table ======
Id     Name   Date               
-----  -----  -------------------  
1      Node1  2015-10-09 22:19:02
2      Node2  2015-10-09 05:41:07
3      Node3  2015-10-09 23:55:13
4      Node4  Today   
```


# Support Chinese 
TODO 中文支持还有问题