import sys
from common import get_sentences, count_commas


def is_complex(sentence):
    return count_commas(sentence) > 1


def find_first_complex():
    for sentence in get_sentences():
        if is_complex(sentence):
            return sentence
    raise ValueError("Not found")


def main():
    try:
        result = find_first_complex()
        print(result)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
