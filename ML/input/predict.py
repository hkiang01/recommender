#!/usr/bin/env python

import sys, re
import numpy as np
from sklearn.externals import joblib
import pandas as pd

def get_top_k_classes(predictions_proba, classes,k):
    ordered_indices_ascending = np.argsort(predictions_proba, axis=1)
    ordered_indices_descending = np.flip(ordered_indices_ascending, axis=1)
    top_k_indices = ordered_indices_descending[:,:k]

    # for each row, for each index, get corresponding class such that each row has the top k classes in a list
    top_k_classes = list(map(lambda row: list(map(lambda i: classes[i], row)), top_k_indices))
    return top_k_classes

def main(argv):
    model_path = 'decision tree classifier.pkl'
    features = pd.read_csv('ML/data_features.csv', header=None)
    stations = pd.read_csv('ML/stations.csv', header=None)
    stations_dict = stations.to_dict(orient='index')
    clf = joblib.load(model_path)

    for line in features.values:
        features = [line[:6]] # clf expects a 2D array
        predictions_probs = clf.predict_proba(features)
        recommendations = get_top_k_classes(predictions_probs, clf.classes_,5)[0]
        recommended_stations = list(map(lambda station_id: stations_dict[station_id][1], recommendations))
        recommendations_string = np.array_str(np.array(recommended_stations))
        line_str = np.array_str(line)
        sys.stdout.write(line_str+'\t'+recommendations_string+'\n')
if __name__ == "__main__":
    main(sys.argv)