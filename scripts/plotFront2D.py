import pandas as pd 
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np
import scipy.stats as stats
import sys


paretoFront = str(sys.argv[1])

f = pd.read_csv(paretoFront, header=0, delimiter="\t")


plt.scatter(f["R{\"TotalLength\"}=? [ S ]"], f["R{\"TotalLost\"}=? [ S ]"])# s=area, c=colors, alpha=0.5)
plt.show()

print("Done")