#output from mapreduce is fed as input
f = open("part-r-00000.txt", "r")

#stores the publications of each author
authNos = {}

#stores the pairs of authors along with their # of collaborations
authCo = {}

for x in f:
    #stores the authors name which can be either comma separated or single authors
    authors = ''.join([i for i in x if not i.isdigit()])
    authors = authors.strip('\n')
    authors = authors.strip('\t')
    authors = authors.replace('.','')
    authors = authors.replace('-','')
    authors = authors.replace(" ", "_")
    count = ''.join([i for i in x if i.isdigit()])
    #if comma separated
    if "," in authors:
        authCo[authors] = count
        #split each author on comma
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

f = open("di_output.txt","w")
f.write("digraph{")

#write the code in DOT format to the output file        
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