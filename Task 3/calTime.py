def calTime(file):
    f = open(file,"r")
    sum = 0;
    for i in f.readlines():
        sum+=int(i)
    print("Average Time for"+file+" is "+sum/len(f.readlines()))
    return sum/len(f.readlines())

    
