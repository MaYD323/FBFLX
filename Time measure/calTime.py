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


calTime("/Users/Harry/Desktop/file/2.4/TJ 2.4.txt")
calTime("/Users/Harry/Desktop/file/2.4/TS 2.4.txt")

