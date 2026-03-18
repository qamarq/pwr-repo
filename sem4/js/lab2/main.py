import sys
import re


def process_line(line):
    line = line.strip()
    line = re.sub(r" +", " ", line)
    return line


def main():
    found_isbn = False

    for line in sys.stdin:
        if not found_isbn:
            if "ISBN" in line:
                found_isbn = True
            continue

        if found_isbn and line.strip() == "":
            break

    for line in sys.stdin:
        if "-----" in line:
            break
        processed = process_line(line)
        print(processed)


if __name__ == "__main__":
    main()
