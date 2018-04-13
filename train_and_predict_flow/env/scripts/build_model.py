#!/usr/bin/env python

import sys, re
import numpy as np
from sklearn import svm
from sklearn.externals import joblib

def main(argv):
    train_file = argv[1]
    model_file = argv[2]

    train = np.loadtxt(train_file, delimiter=',')
    train_label = train[:,0]
    train_features = train[:,[1,2,3,4,5,6]]

    #Export pkl
    clf = svm.SVC()
    clf.fit(train_features, train_label)
    joblib.dump(clf, model_file, compress=9)

if __name__ == "__main__":
    main(sys.argv)