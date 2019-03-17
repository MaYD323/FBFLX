import random
def calTime(file):
    f = open(file,"r")
    sum = 0;
    l = f.readlines()
    for i in l:
        try:
            sum+=int(i.rstrip('\n'))
        except:
            pass
    print("Average Time for"+file+" is "+ str(sum/len(l)/1000000))
    return sum/len(l)


def writefile(TS, TJ):
    list_TS = []
    list_TJ = []
    f_TS = open(TS,"r")
    f_TJ = open(TJ,"r")
    
    l_TS = f_TS.readlines()
    l_TJ = f_TJ.readlines()
    for i in l_TS:
        try:
            list_TS.append(int(i.rstrip('\n')))
        except:
            pass
    for i in l_TJ:
        try:
            list_TJ.append(int(i.rstrip('\n')))
        except:
            pass
    f1 = open("modified TS.txt","w")
    f2 = open("modified TJ.txt","w")
    for i in range(len(list_TS)):
        r = random.randint(-10,100)
        f1.write(str(list_TS[i] - 1000000*r) + "\n")
        f2.write(str(list_TJ[i] - 1000000*r) + "\n")
    return
            
        


'''
writefile("/Users/Harry/Desktop/file/1.1/TS 1.1.txt","/Users/Harry/Desktop/file/1.1/TJ 1.1.txt")
'''
calTime("/Users/Harry/Desktop/file/2.4/TJ 2.4.txt")
calTime("/Users/Harry/Desktop/file/2.4/TS 2.4.txt")

