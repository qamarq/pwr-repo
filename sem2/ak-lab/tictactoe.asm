.data
prompt_rounds:   .asciiz "Podaj liczbe rund (1-5): "
prompt_move:     .asciiz "Twoj ruch. Podaj numer pola (1-9): "
invalid_move:    .asciiz "Nieprawidlowy ruch. Sprobuj ponownie.\n"
comp_move_msg:   .asciiz "Ruch komputera...\n"
newline:         .asciiz "\n"
dashline:        .asciiz "---+---+---\n"
pipe:            .asciiz "|"
space:           .asciiz " "
win_human_msg:   .asciiz "Wygrales runde!\n"
win_comp_msg:    .asciiz "Komputer wygral runde!\n"
draw_msg:        .asciiz "Remis!\n"
final_score_msg: .asciiz "Wynik koncowy: Ty: "
score_mid:       .asciiz ", Komp: "
score_end:       .asciiz ", Remisy: "
score_newline:   .asciiz "\n"

num_rounds:      .word   0
curr_round:      .word   0
wins_h:          .word   0
wins_c:          .word   0
draws:           .word   0
board:           .space  9

# Win conditions (indices of fields)
winconds: .byte 0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6

        .text
        .globl main
main:
    # Wczytaj liczbe rund (1-5)
    la    $a0, prompt_rounds
    li    $v0, 4
    syscall
read_rounds:
    li    $v0, 5
    syscall
    move  $t0, $v0        # t0 = liczba rund
    li    $t1, 1
    li    $t2, 5
    blt   $t0, $t1, read_rounds
    bgt   $t0, $t2, read_rounds
    sw    $t0, num_rounds
    li    $t3, 0
    sw    $t3, wins_h
    sw    $t3, wins_c
    sw    $t3, draws
    li    $t4, 1
    sw    $t4, curr_round

round_loop:
    lw    $t4, curr_round
    lw    $t5, num_rounds
    bgt   $t4, $t5, end_rounds

    # Inicjuj plansze
    la    $t6, board
    li    $t7, 0
init_board:
    sb    $zero, 0($t6)
    addi  $t6, $t6, 1
    addi  $t7, $t7, 1
    li    $t8, 9
    blt   $t7, $t8, init_board
    li    $t9, 0          # liczba ruchow

game_loop:
    # Ruch czlowieka
    la    $a0, prompt_move
    li    $v0, 4
    syscall
read_move:
    li    $v0, 5
    syscall
    move  $t0, $v0
    li    $t1, 1
    li    $t2, 9
    blt   $t0, $t1, invalid_move_h
    bgt   $t0, $t2, invalid_move_h
    la    $t3, board
    addi  $t0, $t0, -1
    add   $t3, $t3, $t0
    lbu   $t4, 0($t3)
    bne   $t4, $zero, invalid_move_h
    li    $t5, 1
    sb    $t5, 0($t3)
    addi  $t9, $t9, 1
    jal   print_board
    jal   check_win
    move  $t6, $v0        # t6 = wynik
    li    $t7, 1
    beq   $t6, $t7, human_wins
    # Remis?
    li    $t1, 9
    beq   $t9, $t1, draw_round

    # Ruch komputera (pierwsze wolne pole)
    la    $a0, comp_move_msg
    li    $v0, 4
    syscall
    la    $t4, board
    li    $t5, 0
find_empty:
    lbu   $t6, 0($t4)
    beq   $t6, $zero, place_comp
    addi  $t4, $t4, 1
    addi  $t5, $t5, 1
    li    $t7, 9
    blt   $t5, $t7, find_empty
    j     draw_round
place_comp:
    li    $t8, 2
    sb    $t8, 0($t4)
    addi  $t9, $t9, 1
    jal   print_board
    jal   check_win
    move  $t6, $v0
    li    $t7, 2
    beq   $t6, $t7, comp_wins
    j     game_loop

invalid_move_h:
    la    $a0, invalid_move
    li    $v0, 4
    syscall
    j     read_move

human_wins:
    la    $a0, win_human_msg
    li    $v0, 4
    syscall
    lw    $t1, wins_h
    addi  $t1, $t1, 1
    sw    $t1, wins_h
    j     end_round

comp_wins:
    la    $a0, win_comp_msg
    li    $v0, 4
    syscall
    lw    $t1, wins_c
    addi  $t1, $t1, 1
    sw    $t1, wins_c
    j     end_round

draw_round:
    la    $a0, draw_msg
    li    $v0, 4
    syscall
    lw    $t1, draws
    addi  $t1, $t1, 1
    sw    $t1, draws

end_round:
    lw    $t2, curr_round
    addi  $t2, $t2, 1
    sw    $t2, curr_round
    j     round_loop

end_rounds:
    # Wyswietl wynik koncowy
    la    $a0, final_score_msg
    li    $v0, 4
    syscall
    lw    $a0, wins_h
    li    $v0, 1
    syscall
    la    $a0, score_mid
    li    $v0, 4
    syscall
    lw    $a0, wins_c
    li    $v0, 1
    syscall
    la    $a0, score_end
    li    $v0, 4
    syscall
    lw    $a0, draws
    li    $v0, 1
    syscall
    la    $a0, score_newline
    li    $v0, 4
    syscall
    li    $v0, 10
    syscall

#----------------------------------------
# Subroutine: print current board
#----------------------------------------
print_board:
    la    $t0, board
    li    $t1, 0
row_loop:
    li    $t2, 0
col_loop:
    # Print leading space
    la    $a0, space
    li    $v0, 4
    syscall
    # Compute index = row*3 + col
    mul   $t3, $t1, 3
    add   $t3, $t3, $t2
    add   $t4, $t0, $t3
    lbu   $t5, 0($t4)
    beq   $t5, $zero, print_empty
    li    $t6, 1
    beq   $t5, $t6, print_X
    # Else print O
    li    $a0, 79        # 'O'
    li    $v0, 11
    syscall
    j     after_char
print_X:
    li    $a0, 88        # 'X'
    li    $v0, 11
    syscall
    j     after_char
print_empty:
    la    $a0, space
    li    $v0, 4
    syscall
after_char:
    # Print trailing space
    la    $a0, space
    li    $v0, 4
    syscall
    # Print '|' if not last col
    li    $t6, 2
    blt   $t2, $t6, pr_pipe
    j     next_col
pr_pipe:
    la    $a0, pipe
    li    $v0, 4
    syscall
next_col:
    addi  $t2, $t2, 1
    li    $t6, 3
    blt   $t2, $t6, col_loop
    # End row
    la    $a0, newline
    li    $v0, 4
    syscall
    # Print dash if not last row
    li    $t6, 2
    blt   $t1, $t6, pr_dash
    j     next_row
pr_dash:
    la    $a0, dashline
    li    $v0, 4
    syscall
next_row:
    addi  $t1, $t1, 1
    li    $t6, 3
    blt   $t1, $t6, row_loop
    jr    $ra

#----------------------------------------
# Subroutine: check for win
# Returns 1 (X), 2 (O) or 0
#----------------------------------------
check_win:
    la    $t0, board       # $t0 = baza planszy
    li    $t1, 0           # i = 0
    li    $t2, 8           # liczba warunków = 8

cw_loop:
    beq   $t1, $t2, no_win # je¿eli i==8 to koniec, brak wygranej

    la    $t3, winconds    # baza tablicy warunków
    mul   $t4, $t1, 3      # t4 = 3*i
    addu  $t3, $t3, $t4    # t3 = &winconds[3*i]

    lb    $t4, 0($t3)      # idx1
    lb    $t5, 1($t3)      # idx2
    lb    $t6, 2($t3)      # idx3

    # wczytaj board[idxX]
    addu  $t7, $t0, $t4
    lbu   $t7, 0($t7)      # a = board[idx1]
    addu  $t8, $t0, $t5
    lbu   $t8, 0($t8)      # b = board[idx2]
    addu  $t9, $t0, $t6
    lbu   $t9, 0($t9)      # c = board[idx3]

    # je¿eli a==b==c i !=0 -> mamy zwyciêzcê
    bne   $t7, $t8, next_cond
    bne   $t7, $t9, next_cond
    beq   $t7, $zero, next_cond

    move  $v0, $t7         # zwyciêzca w $v0
    jr    $ra

next_cond:
    addi  $t1, $t1, 1      # i++
    j     cw_loop

no_win:
    li    $v0, 0
    jr    $ra