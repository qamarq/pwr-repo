import sys
from common import read_stdin, is_whitespace


def count_paragraphs():
    ile_akapitow = 0
    line_content = False
    in_para = False

    for char in read_stdin():
        if char == "\n":
            if line_content:
                if not in_para:
                    ile_akapitow += 1
                    in_para = True
            else:
                if in_para:
                    in_para = False
            line_content = False
        elif not is_whitespace(char):
            line_content = True

    if line_content and not in_para:
        ile_akapitow += 1

    return ile_akapitow


def main():
    try:
        result = count_paragraphs()
        print(result)
    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
