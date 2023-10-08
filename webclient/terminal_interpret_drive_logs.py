import json
import time

j = json.load(open('logs.json'))
current_pos_logs = []
target_pos_logs = []
motor_powers_logs = []
for i in j["logs"]:
    if i["TAG"] == "drive":
        if "Current" in i["message"]:
            current_pos_logs.append(i)
        elif "Target" in i["message"]:
            target_pos_logs.append(i)
        elif "Motor Powers: " in i["message"]:
            motor_powers_logs.append(i)


for i in range(len(current_pos_logs)):
    if not i == 0:
        print("\033[3A", end="")
    print(str(current_pos_logs[i]["message"]))
    print(str(target_pos_logs[i]["message"]))
    print(str(motor_powers_logs[i]["message"]))
    time.sleep(0.1)
