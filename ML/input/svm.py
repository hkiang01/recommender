from sklearn import svm, tree, neighbors, ensemble
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from scipy.stats import itemfreq
import numpy as np

def train_and_predict_top_k(clf, x_train, x_test, y_train, y_test, k):
    clf = clf.fit(x_train, y_train)
    classes = clf.classes_
    predictions = clf.predict_proba(x_test)

    ordered_indices_ascending = np.argsort(predictions, axis=1)
    ordered_indices_descending = np.flip(ordered_indices_ascending,axis=1)
    top_k_indices = ordered_indices_descending[:,:k]

    labels = y_test.values.reshape(len(predictions))
    top_k_classes = list(map(lambda row: list(map(lambda index: classes[index], row)), top_k_indices))
    
    num_correct = 0
    for i in range(0,len(predictions)):
        if labels[i] in top_k_classes[i]:
            num_correct += 1
    
    return num_correct / len(predictions)

def train_and_predict(clf, x_train, x_test, y_train, y_test):
    clf = clf.fit(x_train, y_train)
    # todo: use predict_proba to judge top k score
    predictions = clf.predict(x_test)
    score = accuracy_score(y_test, predictions)
    return score

def run_through_models(x_train, x_test, y_train, y_test):
    print(x_train.shape, y_train.shape)
    print(x_test.shape, y_test.shape)
    item_freq = itemfreq(y_test)
    print(f"there are {len(item_freq)} unique classes in the test set")

    # takes a LONG time
    # clf = svm.SVC()
    # clf.fit(x_train, y_train)
    # predictions = clf.predict(x_test)

    # for n_estimators in range(3,15,1):
    #     clf = ensemble.RandomForestClassifier(n_estimators,max_depth=None,)
    #     score = train_and_predict(clf, x_train, x_test, y_train, y_test)
    #     print(f"model: random forest, n_estimators: {n_estimators}, score: {score}")

    # for weights in ['uniform', 'distance']:
    #     for n_neighbors in range(15,3,-1):
    #         clf = neighbors.KNeighborsClassifier(n_neighbors,weights)
    #         score = train_and_predict(clf, x_train, x_test, y_train, y_test)
    #         print(f"model: knn, weights: {weights}, n_neighbors: {n_neighbors}, score: {score}")

    # seems to perform the best
    clf = tree.DecisionTreeClassifier()
    score = train_and_predict(clf, x_train, x_test, y_train, y_test)
    print(f"model: decision tree, score: {score}")

def main():

    features = pd.read_csv('ML/data_features.csv', header=None)
    labels = pd.read_csv('ML/data_labels.csv', header=None)

    # with all features
    print("BEGIN ALL FEATURES")
    x_large, x_small, y_large, y_small = train_test_split(features, labels, test_size=0.9)
    x_train, x_test, y_train, y_test = train_test_split(x_small, y_small, test_size=0.2)
    run_through_models(x_train, x_test, y_train, y_test)
    print("END ALL FEATURES")

    print()
    print()
    print()

    # sans weather
    print("BEGIN WITHOUT WEATHER")
    x_train = x_train.drop([6,7,8],axis=1)
    x_test = x_test.drop([6,7,8],axis=1)
    run_through_models(x_train, x_test, y_train, y_test)
    print("END WITHOUT WEATHER")  



if __name__ == "__main__":
    main()