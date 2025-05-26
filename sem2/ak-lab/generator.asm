.data
    alphanumeric_chars: .asciiz "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    newline:            .asciiz "\n"

.text
.globl main

main:
    li $v0, 30          # syscall code for time
    syscall             # $a0 bedize ziarnem

    li $v0, 40          # syscall code for srandom
    syscall

    li $t0, 0           # i = 0 - licznik ciagow
    li $s1, 10          # ile wygenerowac

outer_loop:
    beq $t0, $s1, exit_program

    li $t1, 0           # j = 0 - licznik znakow
    li $s2, 10          # ile znakow w ciagu

inner_loop:
    beq $t1, $s2, print_newline_and_continue

    # generaor losowy
    move $a0, $zero     # dolna granica
    li $a1, 62          # gorna granica
    li $v0, 42          # syscall code dla random_int_range
    syscall             # losowy index -> $a0

    # pobranie znaku
    la $t2, alphanumeric_chars # $t2 = adres bazowy alphanumeric_chars
    addu $t3, $t2, $a0  # $t3 = adres znaku (baza + wylosowany indeks)
    lb $a0, 0($t3)      # $a0 = wczytany znak (bajt), gotowy dla print_char

    # i princik
    li $v0, 11          # syscall dla print_char
    syscall

    addi $t1, $t1, 1    # j++ (inc licznik znakow)
    j inner_loop

print_newline_and_continue:
    la $a0, newline
    li $v0, 4           # print_string
    syscall

    addi $t0, $t0, 1    # i++ (inc licznik ciagow)
    j outer_loop

exit_program:
    li $v0, 10          # the end :)
    syscall