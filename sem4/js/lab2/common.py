import sys


def read_stdin():
    while True:
        char = sys.stdin.read(1)
        if not char:
            break
        yield char


def is_whitespace(char):
    return char in " \t\n\r"


def is_sentence_end(char):
    return char in ".!?"


def is_uppercase(char):
    return char.isupper() and char.isalpha()


def is_letter(char):
    return char.isalpha()


def get_sentences():
    current = ""
    for char in read_stdin():
        current += char
        if is_sentence_end(char):
            cleaned = ""
            prev_space = False
            for c in current:
                if is_whitespace(c):
                    if not prev_space and cleaned:
                        cleaned += " "
                        prev_space = True
                else:
                    cleaned += c
                    prev_space = False
            if cleaned and cleaned[-1] == " ":
                cleaned = cleaned[:-1]
            if cleaned:
                yield cleaned
            current = ""


def get_words(sentence):
    word = ""
    for char in sentence:
        if is_letter(char):
            word += char
        else:
            if word:
                yield word
                word = ""
    if word:
        yield word


def ile_slow(sentence):
    count = 0
    for _ in get_words(sentence):
        count += 1
    return count


def count_commas(sentence):
    count = 0
    for char in sentence:
        if char == ",":
            count += 1
    return count


def get_first_word(sentence):
    for word in get_words(sentence):
        return word
    return ""


def words_with_position(sentence):
    first = True
    for word in get_words(sentence):
        yield (word, first)
        first = False


def koniec_pytanie_wykrzyknik(sentence):
    if not sentence:
        return False
    return sentence[-1] in "!?"


def same_start_letter(word1, word2):
    if not word1 or not word2:
        return False
    return word1[0].lower() == word2[0].lower()


def has_uppercase(word):
    for char in word:
        if is_uppercase(char):
            return True
    return False


def starts_uppercase(word):
    if not word:
        return False
    return is_uppercase(word[0])
