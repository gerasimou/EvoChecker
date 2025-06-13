import pandas as pd 
import matplotlib.pyplot as plt
import sys


paretoFront = str(sys.argv[1])

f = pd.read_csv(paretoFront, header=0, delimiter="\t")

plt.scatter(f.iloc[:,0], f.iloc[:,1])# s=area, c=colors, alpha=0.5)
plt.xlabel(f.columns[0])
plt.ylabel(f.columns[1])
plt.show()

print("Done")