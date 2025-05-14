.data
prompt_op:     .asciiz "Podaj rodzaj operacji (0-szyfrowanie, 1-odszyfrowanie): "
prompt_shift:  .asciiz "Podaj przesuniecie: "
prompt_text:   .asciiz "Podaj tekst (max 16 znakow): "
result_text:   .asciiz "Wynik: "
newline:       .asciiz "\n"
buffer:        .space 18

.text
.globl main
main:
    li   $v0, 4
    la   $a0, prompt_op
    syscall

    li   $v0, 5
    syscall
    move $t0, $v0

    li   $v0, 4
    la   $a0, prompt_shift
    syscall

    li   $v0, 5
    syscall
    move $t1, $v0

    li   $v0, 4
    la   $a0, prompt_text
    syscall

    la   $a0, buffer
    li   $a1, 18
    li   $v0, 8
    syscall

    beq  $t0, 1, neg_shift
    j    shift_ready

neg_shift:
    sub  $t1, $zero, $t1

shift_ready:
    la   $s1, buffer
    move $s0, $s1

process_loop:
    lb   $t2, 0($s0)
    beq  $t2, $zero, end_process
    li   $t3, 10
    beq  $t2, $t3, terminate
    j    convert

terminate:
    sb   $zero, 0($s0)
    j    end_process

convert:
    li   $t3, 97
    blt  $t2, $t3, check_upper
    li   $t3, 122
    bgt  $t2, $t3, check_upper
    addi $t2, $t2, -32

check_upper:
    li   $t3, 65
    blt  $t2, $t3, next_char
    li   $t3, 90
    bgt  $t2, $t3, next_char
    addi $t2, $t2, -65
    add  $t2, $t2, $t1
    li   $t3, 26
    div  $t2, $t3
    mfhi $t2
    blt  $t2, $zero, add_26
    j    store_char

add_26:
    add  $t2, $t2, $t3

store_char:
    addi $t2, $t2, 65
    sb   $t2, 0($s0)

next_char:
    addi $s0, $s0, 1
    j    process_loop

end_process:
    li   $v0, 4
    la   $a0, newline
    syscall

    li   $v0, 4
    la   $a0, result_text
    syscall

    li   $v0, 4
    la   $a0, buffer
    syscall

    li   $v0, 10
    syscall
