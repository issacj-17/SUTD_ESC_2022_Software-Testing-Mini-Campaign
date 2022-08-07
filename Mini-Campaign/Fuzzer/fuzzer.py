import csv
import subprocess
import random
import time
import string
from faker import Faker

def create_csv_headers(num_rows):
    fake = Faker()
    headers = ('Name', 'Address', 'City', 'Country', 'Email', 'Account No.', 'Credit Card Number', 'Currency', 'Balance', 'Timestamp')
    columns = ('{{name}}', '{{street_address}}', '{{city}}', '{{country}}', '{{ascii_free_email}}', '{{iban}}','{{credit_card_number}}','{{currency_code}}', '{{pricetag}}', '{{date_time_this_year}}')
    results = fake.csv(header=headers, data_columns=columns, num_rows=num_rows, include_row_ids=False)

    filename = "testfile"
    filepath = f".\\testfile\\{filename}.csv"
    with open(filepath, "w", newline="", encoding="utf-8") as file:
        file.write(results)
    file.close()

    return filepath, filename

def create_csv_no_headers(num_rows):
    fake = Faker()
    columns = ('{{name}}', '{{street_address}}', '{{city}}', '{{country}}', '{{ascii_free_email}}', '{{iban}}','{{credit_card_number}}','{{currency_code}}', '{{pricetag}}', '{{date_time_this_year}}')
    results = fake.csv(data_columns=columns, num_rows=num_rows, include_row_ids=False)

    filename = "testfile"
    filepath = f".\\testfile\\{filename}.csv"
    with open(filepath, "w", newline="", encoding="utf-8") as file:
        file.write(results)
    file.close()

    return filepath, filename

def mutate_csv_file(filepath, filename):
    with open(filepath, "r") as file:
        result = file.readlines()
    file.close()

    for i in range(len(result)):
        strVal = result[i]
        # print(strVal)
        strOut = list(strVal)
        j = random.randrange(0, len(strOut))
        strOut[j] = random.choice(string.ascii_letters)
        strFinal = "".join(strOut)
        # print(strFinal)
        result[i] = strFinal

    mutated_filename = f"mutated_{filename}.csv"
    mutated_filepath = f".\\testfile\\{mutated_filename}"
    with open(mutated_filepath, "w") as file:
        file.writelines(result)
    file.close()

    return mutated_filepath

def mutate_string(command):
    cmdList = list(command)
    j = random.randrange(0, len(cmdList))
    cmdList[j] = random.choice(string.ascii_letters)
    strFinal = "".join(cmdList)
    return strFinal

def log_report(result):
    with open('.\\testfile\\output.log', 'ab') as file:
        newline = str.encode("\n", encoding="utf-8")
        file.write(result.stdout)
        file.write(result.stderr)
        file.write(newline)
    file.close()

def main(limit, num_rows):
    start = time.time()
    filepath1, filename1 = create_csv_headers(num_rows)

    filepath2 = mutate_csv_file(filepath1, filename1)

    for i in range(limit):
        commandargs = f"{filepath1} {filepath2} -d , -p true"
        commandargs = mutate_string(commandargs)
        command = "java -classpath ..\\target\\classes\\ Main " + commandargs
        result = subprocess.run(command, capture_output=True, shell=True)
        log_report(result)
        print(f"ValidateArgs: Trial {i+1} completed")

    for i in range(limit):
        filepath2 = mutate_csv_file(filepath1, filename1)
        command = f"java -classpath ..\\target\\classes\\ Main {filepath1} {filepath2} -d , -p true"
        result = subprocess.run(command, capture_output=True, shell=True)
        log_report(result)
        print(f"With Headers: Trial {i+1} completed")

    filepath1, filename1 = create_csv_no_headers(num_rows)
    for i in range(limit):
        filepath2 = mutate_csv_file(filepath1, filename1)
        command = f"java -classpath ..\\target\\classes\\ Main {filepath1} {filepath2} -d , -p false"
        result = subprocess.run(command, capture_output=True, shell=True)
        log_report(result)
        print(f"Without Headers: Trial {i+1} completed")

    elapsed = time.time() - start
    print(f"Testing Completed - Time Taken: {elapsed}s")
    print("Check output.log file in Fuzzer\\testfile Directory to view the results of the Fuzzing")

if __name__ == "__main__":
    limit = 150
    num_rows = 600
    main(limit, num_rows)
