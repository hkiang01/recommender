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
    month_switch = {
        'Jan': '1',
        'Feb': '2',
        'Mar': '3',
        'Apr': '4',
        'May': '5',
        'Jun': '6',
        'Jul': '7',
        'Aug': '8',
        'Sep': '9',
        'Oct': '10',
        'Nov': '11',
        'Dec': '12'
    }

    s = start_time.split(' ')
    return month_switch.get(s[1], '0')

def get_start_hour(start_time):
    raw = re.sub('^.* ', '', re.sub(':.*', '', start_time))
    formatted = str(int(raw))
    return formatted

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

user_type_switch = {
    'Customer': '1',
    'Subscriber': '2'
}

def get_user_type(user_type):
    return user_type_switch.get(user_type, '0')

dataset_base_dir = "/home/harry/projects/school/recommender/ML/cca_dataset/"
for i, file in enumerate(os.listdir(dataset_base_dir)):
    if i > 0:
        continue
    file_name = dataset_base_dir + file
    print(file_name)
    fr = open(file_name, 'r')
    for j, line in enumerate(fr):
        if j>5:
            continue
        print(str(i) + " " + str(j) + " "  + line)
        s = line.split(",")
        month = get_start_month(s[1])
        print("month: " + month)
        hour = get_start_hour(s[1])
        print("hour: " + hour)
        from_station_id=s[5]
        print("from station id: " + from_station_id)
        gender=get_gender_num(s[10])
        print("gender: " + gender)
        birthyear=get_birthyear(s[11])
        print("birthyear: " + birthyear)
        usertype=get_user_type(s[9])
        print("usertype: " + usertype)
        avg_max_temp=str(round(float(s[12])))
        print("avg max temp: " + avg_max_temp)
        avg_min_temp=str(round(float(s[13])))
        print("avg min temp: " + avg_min_temp)
        avg_obs_temp=str(round(float(s[14])))
        print("avg obs temp: " + avg_obs_temp)
        features = (month+
        ','+hour+
        ','+from_station_id+
        ','+gender+
        ','+birthyear+
        ','+usertype+
        ','+avg_min_temp+
        ','+avg_max_temp+
        ','+avg_obs_temp)
        label = s[7]
        print(features)
        print(label)
    fr.close()

# features = list()
# labels = list()
# fr = open('/home/harry/projects/school/recommender/ML/input/Divvy_Trips_2017_Q4.csv', 'r')
# for i, line in enumerate(fr):
#     if i > 0:
#         line = re.sub("[\r\n]", '', line)
#         s = line.split(',')
#         print(str(len(s)))
#     # if len(s) != 12 or re.search('start_time', line):
#     #     continue
#     # month = get_start_month(s[1])
#     # hour = get_start_hour(s[1])
#     # tripduration=get_tripduration_min(s[4])
#     # from_station_id=s[5]
#     # to_station_id=s[7]
#     # gender=get_gender_num(s[10])
#     # birthyear=get_birthyear(s[11])
#     # features.append(month+','+hour+','+from_station_id+','+gender+','+birthyear)
#     # # features.append(month + ',' + hour)
#     # labels.append(to_station_id)
# fr.close()

# random_index_set = get_random_index_set(len(labels))

# fw_train = open('train_feature_label.txt', 'w')
# #fw_test = open('test_feature_label.txt', 'w')
# fw_test_feature = open('test_feature.txt', 'w')
# fw_test_label = open('test_label.txt', 'w')
# for i in range(0, len(labels)):
#     if i not in random_index_set:
#         #fw_train.write(features[i]+','+labels[i]+'\n')
# 	fw_train.write(labels[i]+','+features[i]+'\n')
#     else:
#         #fw_test.write(features[i]+','+labels[i]+'\n')
# 	#w_test.write(labels[i]+','+features[i]+'\n')
#         fw_test_feature.write(features[i]+'\n')
#         fw_test_label.write(labels[i]+'\n')
# fw_train.close()
# #fw_test.close()
# fw_test_feature.close()
# fw_test_label.close()
