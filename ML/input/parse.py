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
features_file = open('ML/data_features.csv', 'w')
labels_file = open('ML/data_labels.csv', 'w')
for i, file in enumerate(os.listdir(dataset_base_dir)):
    file_name = dataset_base_dir + file
    print(file_name)
    fr = open(file_name, 'r')
    for j, line in enumerate(fr):
        if "to_station" in line:
            continue
        s = line.split(",")
        month = get_start_month(s[1])
        hour = get_start_hour(s[1])
        from_station_id=s[5]
        gender=get_gender_num(s[10])
        birthyear=get_birthyear(s[11])
        usertype=get_user_type(s[9])
        if(s[12].strip()=='' or s[12].strip()==None):
            avg_max_temp = ''
        else:
            avg_max_temp=str(round(float(s[12].strip())))
        if(s[13].strip()=='' or s[13].strip()==None):
            avg_min_temp = ''
        else:
            avg_min_temp=str(round(float(s[13].strip())))
        if(s[14].strip()=='' or s[14].strip()==None):
            avg_obs_temp=''
        else:
            avg_obs_temp=str(round(float(s[14].strip())))
        features = (month+
        ','+hour+
        ','+from_station_id+
        ','+gender+
        ','+birthyear+
        ','+usertype+
        ','+avg_min_temp+
        ','+avg_max_temp+
        ','+avg_obs_temp)
        if '' in features.split(','):
            continue
        label = s[7]
        features_file.write(features + "\n")
        labels_file.write(label + "\n")
    fr.close()

features_file.close()
labels_file.close()
