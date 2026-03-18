import sys
from common import get_sentences, words_with_position, starts_uppercase


def has_proper_noun(sentence):
    for word, pierwszy in words_with_position(sentence):
        if not pierwszy and starts_uppercase(word):
            return True
    return False


def calculate_percent():
    total = 0
    with_names = 0

    for sentence in get_sentences():
        total += 1
        if has_proper_noun(sentence):
            with_names += 1

    if total == 0:
        raise ValueError("No sentences found")

    procent = (with_names / total) * 100
    return procent


def main():
    try:
        result = calculate_percent()
        print(f"{result:.2f}")
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
