f = open("part-r-00000.txt", "r")

authNos = {}
authCo = {}
for x in f:
    authors = ''.join([i for i in x if not i.isdigit()])
    authors = authors.strip('\n')
    authors = authors.strip('\t')
    authors = authors.replace('.','')
    authors = authors.replace('-','')
    authors = authors.replace(" ", "_")
    count = ''.join([i for i in x if i.isdigit()])
    if "," in authors:
        authCo[authors] = count
        author_pair = [x.strip() for x in authors.split(',')]

        if author_pair[0] not in authNos:
            authNos[author_pair[0]] = count
        else:
            authNos[author_pair[0]] = str(int(authNos[author_pair[0]])+int(count))
        
        if author_pair[1] not in authNos:
            authNos[author_pair[1]] = count
        else:
            authNos[author_pair[1]] = str(int(authNos[author_pair[1]])+int(count))
    else:
        authNos[authors] = count
#print(authNos)
print(authCo)

f = open("di_output.txt","w")
f.write("digraph{")
for x,y in authNos.items():    
    if(int(y) < 20):
        y = 20
        f.write(x+"[fontsize=\""+str(y)+"\"];")
    else:
        f.write(x+"[fontsize=\""+str(y)+"\"];")
    
for x,y in authCo.items():
    x = [k.strip() for k in x.split(',')]
    f.write(x[0]+"->"+x[1]+"[label=\""+str(y)+"\",penwidth=\"7\",fontsize=\"120\""+"];")
    
f.write("}")    