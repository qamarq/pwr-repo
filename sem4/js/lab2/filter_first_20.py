import sys
from common import get_sentences


def first_n(n=20):
    count = 0
    for sentence in get_sentences():
        if count >= n:
            break
        yield sentence
        count += 1


def main():
    try:
        for sentence in first_n(20):
            print(sentence)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
