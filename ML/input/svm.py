import numpy as np
from sklearn import svm, tree, neighbors, datasets, ensemble
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import pickle
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap

def train_and_predict(clf, x_train, y_train, x_test, y_test):
    clf = clf.fit(x_train, y_train)
    predictions = clf.predict(x_test)
    score = accuracy_score(y_test, predictions)
    return score

def main():

    features = pd.read_csv('ML/data_features.csv')
    labels = pd.read_csv('ML/data_labels.csv')

    x_large, x_small, y_large, y_small = train_test_split(features, labels, test_size=0.3)

    x_train, x_test, y_train, y_test = train_test_split(x_small, y_small, test_size=0.2)
    print(x_train.shape, y_train.shape)
    print(x_test.shape, y_test.shape)

    # clf = svm.SVC()
    # clf.fit(x_train, y_train)
    # predictions = clf.predict(x_test)

    for n_estimators in range(3,15,1):
        clf = ensemble.RandomForestClassifier(n_estimators,max_depth=None,)
        score = train_and_predict(clf, x_train, y_train, x_test, y_test)
        print(f"model: random forest, n_estimators: {n_estimators}, score: {score}")

    for weights in ['uniform', 'distance']:
        for n_neighbors in range(15,3,-1):
            clf = neighbors.KNeighborsClassifier(n_neighbors,weights)
            score = train_and_predict(clf, x_train, y_train, x_test, y_test)
            print(f"model: knn, weights: {weights}, n_neighbors: {n_neighbors}, score: {score}")

    clf = tree.DecisionTreeClassifier()
    score = train_and_predict(clf, x_train, y_train, x_test, y_test)
    print("model: decision tree, score: {score}")

    # pd.DataFrame(y_test).to_csv('ML/y_test.csv')
    # pd.DataFrame(predictions).to_csv('ML/predictions.csv')

if __name__ == "__main__":
    main()