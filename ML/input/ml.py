from sklearn import svm, tree, neighbors, ensemble
import pandas as pd
from sklearn.model_selection import train_test_split
from scipy.stats import itemfreq
import numpy as np

def accuracy_top_k(predictions_proba, labels, classes, k):
    """Calculates top-k accuracy.
    Top-k accuracy means that any of k highest probability answers must match the expected answer.

    For example, consider the following probabilities for a prediction in descending order:

    'class_0': 0.50,
    'class_1': 0.40,
    'class_2': 0.35,
    ...

    If the expected answer is 'class_1' and k=1, the prediction is wrong.
    If the expected answer is 'class_1' and k=2, the prediction is correct.

    Example
    -------
    from sklearn import tree
    clf = clf.fit(x_train, y_train)
    classes = clf.classes_
    predictions_proba = clf.predict_proba(x_test)
    labels = y_test.values.reshape(len(y_test))
    score = accuracy_top_k(predictions_proba, labels, classes, k)

    Parameters
    -------
    predictions_proba : (ndarray) : 2D array containing data of type `float`, the probabilities of each class in classes.
                              For example, if predictions_proba[row][i] = 0.05,
                              and the value of the actual predictions_proba is classes[i],
                              then there is a 0.05 probability that the prediction is classes[i].
                              The number of columns of predictions_proba must be the same as the size of labels.
    labels : (ndarray): 1D array containing data, the expected predictions. This size of
                        labels must be the same as the number of predictions.
    classes : (ndarray): 1D array containing list of classes whose type matches that of labels.
                         These are the possible prediction values.
                         The size of classes should correspond to the number of columns of predictions_proba.
    k : (int) : The top number of predictions to preserve in descending order of probability.
    Returns
    -------
    float : The top-k accuracy, ranging from 0.0 to 1.0
    """
    ordered_indices_ascending = np.argsort(predictions_proba, axis=1)
    ordered_indices_descending = np.flip(ordered_indices_ascending,axis=1)
    top_k_indices = ordered_indices_descending[:,:k]

    # for each row, for each index, get corresponding class such that each row has the top k classes in a list
    top_k_classes = list(map(lambda row: list(map(lambda i: classes[i], row)), top_k_indices))

    # for each row, if the label is contained within the top k classes, 1, else 0
    individual_accuracies = map(lambda row: 1 if labels[row] in top_k_classes[row] else 0, range(0,len(predictions_proba)-1))
    return sum(individual_accuracies) / len(predictions_proba)

def train_and_predict_top_k(clf, x_train, x_test, y_train, y_test, k):
    clf = clf.fit(x_train, y_train)
    classes = clf.classes_
    predictions_proba = clf.predict_proba(x_test)
    labels = y_test.values.reshape(len(y_test))
    score = accuracy_top_k(predictions_proba, labels, classes, k)
    return score

def run_through_models(x_train, x_test, y_train, y_test):
    print(x_train.shape, y_train.shape)
    print(x_test.shape, y_test.shape)
    item_freq = itemfreq(y_test)
    print(f"there are {len(item_freq)} unique classes in the test set")

    clf = tree.DecisionTreeClassifier()
    score = train_and_predict_top_k(clf, x_train, x_test, y_train, y_test, 5)
    print(f"model: decision tree, score: {score}")

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