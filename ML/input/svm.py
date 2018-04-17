import numpy as np
from sklearn import svm
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import pickle

INPUTFILE_PROCESSED=''

def main():

    features = pd.read_csv('ML/data_features.csv')
    labels = pd.read_csv('ML/data_labels.csv')

    x_large, x_small, y_large, y_small = train_test_split(features, labels, test_size=0.05, random_state=12345)

    x_train, x_test, y_train, y_test = train_test_split(x_small, y_small, test_size=0.2, random_state=45678)
    print(x_train.shape, y_train.shape)
    print(x_test.shape, y_test.shape)

    clf = svm.SVC()
    clf.fit(x_train, y_train)

    predictions = clf.predict(x_test)

    y_test.to_csv('y_test.csv')
    predictions.to_csv('predictions.csv')

    score = accuracy_score(y_test, predictions)
    print(score)

if __name__ == "__main__":
    main()