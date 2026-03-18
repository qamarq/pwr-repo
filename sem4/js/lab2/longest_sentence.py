import sys
from common import get_sentences


def find_longest():
    longest = ""
    max_len = 0

    for sentence in get_sentences():
        length = len(sentence)
        if length > max_len:
            max_len = length
            longest = sentence

    if not longest:
        raise ValueError("No sentences found")

    return longest


def main():
    try:
        result = find_longest()
        print(result)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
