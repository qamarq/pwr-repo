import sys
from common import get_sentences, get_words, same_start_letter


def no_repeat(sentence):
    prev = ""
    for word in get_words(sentence):
        if prev and same_start_letter(prev, word):
            return False
        prev = word
    return True


def find_longest():
    longest = ""
    max_len = 0

    for sentence in get_sentences():
        if no_repeat(sentence):
            dlugosc = len(sentence)
            if dlugosc > max_len:
                max_len = dlugosc
                longest = sentence

    if not longest:
        raise ValueError("Not found")

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
