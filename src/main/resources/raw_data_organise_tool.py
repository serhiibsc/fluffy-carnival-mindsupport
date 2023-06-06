import json


def add_match_ids_to_options(json_data):
    counter = 1

    for question in json_data:
        question['questionId'] = str(counter)
        counter += 1
        # options_dict = {str(counter + i): option for i, option in enumerate(question["options"])}
        # question["options"] = options_dict
        # counter += len(question["options"])


def main():
    # Load your existing JSON data
    with open('questions_to_use.json', 'r') as f:
        json_data = json.load(f)

    # Add match IDs to the options
    add_match_ids_to_options(json_data)

    # Write the modified JSON data back to the file
    with open('questions_to_use.json', 'w') as f:
        json.dump(json_data, f, indent=2)


if __name__ == '__main__':
    main()
