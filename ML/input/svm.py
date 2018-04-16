import numpy as np
from sklearn import svm
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

INPUTFILE_PROCESSED=''

def main():

    features = pd.read_csv('ML/data_features.csv')
    labels = pd.read_csv('ML/data_labels.csv')

    x_train, x_test, y_train, y_test = train_test_split(features, labels, test_size=0.2)
    print(x_train.shape, y_train.shape)
    print(x_test.shape, y_test.shape)

    clf = svm.SVC()
    clf.fit(x_train, y_train)

    predictions = clf.predict(x_test)
    score = accuracy_score(y_test, predictions)
    print(score)

if __name__ == "__main__":
    main()