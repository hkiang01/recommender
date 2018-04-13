#!/usr/bin/env python

import sys, re
import numpy as np
from sklearn.externals import joblib

def main(argv):
    for line in sys.stdin:
        line = re.sub('\n', '', line)
        clf_from_file = joblib.load(argv[1])
        input_arr = np.fromstring(line, sep=',')
        features = np.transpose(input_arr.reshape(input_arr.shape[0], -1))
        sys.stdout.write(line+'\t'+str(clf_from_file.predict(features)))
if __name__ == "__main__":
    main(sys.argv)