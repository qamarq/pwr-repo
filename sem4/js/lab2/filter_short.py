import sys
from common import get_sentences, ile_slow


def filter_short():
    for sentence in get_sentences():
        if ile_slow(sentence) <= 4:
            yield sentence


def main():
    try:
        for sentence in filter_short():
            print(sentence)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
