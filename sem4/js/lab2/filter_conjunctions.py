import sys
from common import get_sentences, get_words


def count_spojniki(sentence):
    count = 0
    for word in get_words(sentence):
        word_lower = ""
        for c in word:
            word_lower += c.lower()
        if (
            word_lower == "i"
            or word_lower == "oraz"
            or word_lower == "ale"
            or word_lower == "że"
            or word_lower == "lub"
        ):
            count += 1
    return count


def filter_conjunctions():
    for sentence in get_sentences():
        if count_spojniki(sentence) >= 2:
            yield sentence


def main():
    try:
        for sentence in filter_conjunctions():
            print(sentence)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
