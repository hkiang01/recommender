import numpy as np
from sklearn import svm

INPUTFILE_PROCESSED=''

def main():
    train = np.loadtxt('train_feature_label.small.txt', delimiter=',')
    train_label = train[:,0]
    train_features = train[:,[1,2,3,4,5,6]]
    # print train_features.shape

    clf = svm.SVC()
    clf.fit(train_features, train_label)

    test_features = np.loadtxt('test_feature.small.txt', delimiter=',')
    # print test_features.shape
    test_predict = clf.predict(test_features)
    print test_predict

if __name__ == "__main__":
    main()