"""
writes mapping of station id to name in a csv file
"""

import os

# file to write station maps to
stations_file = open('ML/stations.csv', 'w')
stations_dict = {}

# base folder containing features that contain station data
dataset_base_dir = "/home/harry/projects/school/recommender/ML/cca_dataset/"
for i, file in enumerate(os.listdir(dataset_base_dir)):
    file_name = dataset_base_dir + file
    print(file_name)
    fr = open(file_name, 'r')
    for j, line in enumerate(fr):
        if "to_station" in line:
            continue
        s = line.split(",")
        from_station_id=s[5]
        from_station_name=s[6]
        to_station_id=s[7]
        to_station_name=s[8]
        stations_dict[from_station_id] = from_station_name
        stations_dict[to_station_id] = to_station_name
    fr.close()

unique_stations = {}
for k,v in stations_dict.items():
    k = k.replace('"','')
    v = v.replace('"','')
    if k not in unique_stations.keys():
        unique_stations[k] = v

for k,v in unique_stations.items():
    stations_file.write(k+","+v+"\n")

stations_file.close()