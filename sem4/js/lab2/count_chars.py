import sys
from common import read_stdin, is_whitespace


def count_chars():
    count = 0
    for char in read_stdin():
        if not is_whitespace(char):
            count += 1
    return count


def main():
    try:
        wynik = count_chars()
        print(wynik)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
