import sys
from common import get_sentences, get_words, same_start_letter


def no_repeat(sentence):
    prev = ""
    for word in get_words(sentence):
        if prev and same_start_letter(prev, word):
            return False
        prev = word
    return True


def find_longest_than_6words():
    min_len = 6

    for sentence in get_sentences():
        if "Ta lektura, podobnie" in sentence:
            break
        if no_repeat(sentence):
            dlugosc = 0
            for _ in get_words(sentence):
                dlugosc += 1
            if dlugosc >= min_len:
                print(sentence)


def main():
    try:
        find_longest_than_6words()
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
