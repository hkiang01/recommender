import re,os,random
import numpy
from sklearn.model_selection import train_test_split

def get_random_index_set(size):
    test_size = 0.2 * size
    index = set()
    while (len(index) < test_size):
        index.add(random.randint(0, size-1))
    return index

def get_start_month(start_time):
    s = start_time.split('/')
    month = s[0]
    return month

def get_start_hour(start_time):
    return re.sub('^.* ', '', re.sub(':.*', '', start_time))

def get_gender_num(gender):
    if gender.lower() == 'male':
        return '1'
    elif gender.lower() == 'female':
        return '2'
    else:
        return '0'

def get_birthyear(birthyear):
    if birthyear == '':
        return '0'
    else:
        return birthyear

def get_tripduration_min(tripduration):
    return str(int(tripduration)/60)

features = list()
labels = list()
fr = open('Divvy_Trips_2017_Q4.csv', 'r')
for line in fr:
    line = re.sub("[\r\n]", '', line)
    s = line.split(',')
    if len(s) != 12 or re.search('start_time', line):
        continue
    month = get_start_month(s[1])
    hour = get_start_hour(s[1])
    tripduration=get_tripduration_min(s[4])
    from_station_id=s[5]
    to_station_id=s[7]
    gender=get_gender_num(s[10])
    birthyear=get_birthyear(s[11])
    features.append(month+','+hour+','+tripduration+','+from_station_id+','+gender+','+birthyear)
    # features.append(month + ',' + hour)
    labels.append(to_station_id)
fr.close()

random_index_set = get_random_index_set(len(labels))

fw_train = open('train_feature_label.txt', 'w')
#fw_test = open('test_feature_label.txt', 'w')
fw_test_feature = open('test_feature.txt', 'w')
fw_test_label = open('test_label.txt', 'w')
for i in range(0, len(labels)):
    if i not in random_index_set:
        #fw_train.write(features[i]+','+labels[i]+'\n')
	fw_train.write(labels[i]+','+features[i]+'\n')
    else:
        #fw_test.write(features[i]+','+labels[i]+'\n')
	#w_test.write(labels[i]+','+features[i]+'\n')
        fw_test_feature.write(features[i]+'\n')
        fw_test_label.write(labels[i]+'\n')
fw_train.close()
#fw_test.close()
fw_test_feature.close()
fw_test_label.close()
