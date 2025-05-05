.data
prompt1: .asciiz "Podaj pierwszy argument: "
prompt2: .asciiz "Podaj kod operacji (0-dodawanie, 1-odejmowanie, 2-dzielenie, 3-mnozenie): "
prompt3: .asciiz "Podaj drugi argument: "
result_text: .asciiz "Wynik: "
continue_prompt: .asciiz "Czy chcesz kontynuowac? (1 - tak, 0 - nie): "
newline: .asciiz "\n"

.text
.globl main

main:
loop:
    li $v0, 4
    la $a0, prompt1
    syscall

    li $v0, 5
    syscall
    move $t0, $v0

    li $v0, 4
    la $a0, prompt2
    syscall

    li $v0, 5
    syscall
    move $t1, $v0

    li $v0, 4
    la $a0, prompt3
    syscall

    li $v0, 5
    syscall
    move $t2, $v0

    li $v0, 4
    la $a0, result_text
    syscall

    li $v0, 1
    beq $t1, 0, add_op
    beq $t1, 1, sub_op
    beq $t1, 2, div_op
    beq $t1, 3, mul_op
    j end_op

add_op:
    add $t3, $t0, $t2
    move $a0, $t3
    syscall
    j end_op

sub_op:
    sub $t3, $t0, $t2
    move $a0, $t3
    syscall
    j end_op

div_op:
    beq $t2, $zero, end_op
    div $t0, $t2
    mflo $t3
    move $a0, $t3
    syscall
    j end_op

mul_op:
    mul $t3, $t0, $t2
    move $a0, $t3
    syscall

end_op:
    li $v0, 4
    la $a0, newline
    syscall

    li $v0, 4
    la $a0, continue_prompt
    syscall

    li $v0, 5
    syscall
    beq $v0, 1, loop

    li $v0, 10
    syscall
