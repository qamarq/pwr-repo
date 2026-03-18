import sys
from common import get_sentences, koniec_pytanie_wykrzyknik


def filter_questions():
    for sentence in get_sentences():
        if koniec_pytanie_wykrzyknik(sentence):
            yield sentence


def main():
    try:
        for sentence in filter_questions():
            print(sentence)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
