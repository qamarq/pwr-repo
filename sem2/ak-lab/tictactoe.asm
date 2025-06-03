.data
prompt_rounds: .asciiz "Podaj liczbe rund (1-5): "
prompt_move:   .asciiz "Twoj ruch - podaj numer pola (1-9): "
invalid_move:  .asciiz "Nieprawidlowy ruch, sprobuj ponownie.\n"
win_human_msg: .asciiz "Wygrales!\n"
win_comp_msg:  .asciiz "Komputer wygral!\n"
draw_msg:      .asciiz "Remis!\n"
final_msg:     .asciiz "Wyniki: Gracz: "
final_comp:    .asciiz " Komputer: "
final_draws:   .asciiz " Remisy: "
newline:       .asciiz "\n"
char_dot:      .asciiz "."
char_x:        .asciiz "X"
char_o:        .asciiz "O"
char_bar:      .asciiz "|"

# tablica 9 pól (s³owa 4?bajtowe)
board:         .space 36

# wzorce zwyciêstwa (8 linii po 3 indeksy)
win_patterns:
        .word 0,1,2
        .word 3,4,5
        .word 6,7,8
        .word 0,3,6
        .word 1,4,7
        .word 2,5,8
        .word 0,4,8
        .word 2,4,6

        .text
        .globl main

# ----------------------------------------
# main: czyta liczbê rund, wywo³uje play_round,
# zbiera statystyki, na koniec drukuje wynik
# ----------------------------------------
main:
        # zerujemy liczniki
        li      $s0,0        # human wins
        li      $s1,0        # comp wins
        li      $s2,0        # draws

read_rounds:
        li      $v0,4
        la      $a0,prompt_rounds
        syscall

        li      $v0,5        # read int
        syscall
        move    $t0,$v0
        blt     $t0,1,read_rounds
        bgt     $t0,5,read_rounds
        move    $t1,$t0      # t1 = liczba rund
        li      $t2,0        # aktualna runda

round_loop:
        jal     play_round
        move    $t3,$v0      # kod zwyciêzcy: 0=remis,1=human,2=comp
        li      $t4,1
        beq     $t3,$t4,human_won
        li      $t4,2
        beq     $t3,$t4,comp_won
        # remis
        addi    $s2,$s2,1
        j       end_round

human_won:
        addi    $s0,$s0,1
        j       end_round

comp_won:
        addi    $s1,$s1,1

end_round:
        addi    $t2,$t2,1
        blt     $t2,$t1,round_loop

        # drukujemy statystyki
        li      $v0,4
        la      $a0,final_msg
        syscall

        li      $v0,1
        move    $a0,$s0
        syscall

        li      $v0,4
        la      $a0,final_comp
        syscall

        li      $v0,1
        move    $a0,$s1
        syscall

        li      $v0,4
        la      $a0,final_draws
        syscall

        li      $v0,1
        move    $a0,$s2
        syscall

        li      $v0,4
        la      $a0,newline
        syscall

        li      $v0,10       # exit
        syscall

# ----------------------------------------
# play_round: rozgrywa jedn¹ rundê
# zwraca w $v0: 1=human,2=comp,0=remis
# ----------------------------------------
play_round:
        addi    $sp,$sp,-4
        sw      $ra,0($sp)

        jal     init_board

loop_game:
        # ruch cz³owieka
        jal     human_move
        jal     print_board

        jal     check_win
        li      $t0,1
        beq     $v0,$t0,ret_human

        jal     board_full
        bne     $v0,$zero,ret_draw

        # ruch komputera
        jal     comp_move
        jal     print_board

        jal     check_win
        li      $t0,2
        beq     $v0,$t0,ret_comp

        jal     board_full
        bne     $v0,$zero,ret_draw

        j       loop_game

ret_human:
        # komunikat i powrót kodu=1
        li      $v0,4
        la      $a0,win_human_msg
        syscall
        li      $v0,1
        move    $a0,$zero   # tu $a0 dowolnie
        # zwracamy 1
        li      $v0,1
        lw      $ra,0($sp)
        addi    $sp,$sp,4
        jr      $ra

ret_comp:
        li      $v0,4
        la      $a0,win_comp_msg
        syscall
        # zwracamy 2
        li      $v0,2
        lw      $ra,0($sp)
        addi    $sp,$sp,4
        jr      $ra

ret_draw:
        li      $v0,4
        la      $a0,draw_msg
        syscall
        # zwracamy 0
        li      $v0,0
        lw      $ra,0($sp)
        addi    $sp,$sp,4
        jr      $ra

# ----------------------------------------
# init_board: ustawia wszystkie pola na 0
# ----------------------------------------
init_board:
        la      $t0,board
        li      $t1,9
init_loop:
        sw      $zero,0($t0)
        addi    $t0,$t0,4
        addi    $t1,$t1,-1
        bgtz    $t1,init_loop
        jr      $ra

# ----------------------------------------
# print_board: drukuje 3x3:
#  .|X|O
#  X|.|.
#  .|O|X
# ----------------------------------------
print_board:
        la      $t0,board
        li      $t1,0        # rz¹d
row_loop:
        li      $t2,0        # kolumna
cell_loop:
        lw      $t3,0($t0)
        beq     $t3,$zero,p_dot
        li      $t4,1
        beq     $t3,$t4,p_x
        # w pp. 'O'
        li      $v0,4
        la      $a0,char_o
        syscall
        j       after_cell

p_dot:
        li      $v0,4
        la      $a0,char_dot
        syscall
        j       after_cell

p_x:
        li      $v0,4
        la      $a0,char_x
        syscall

after_cell:
        addi    $t2,$t2,1
        beq     $t2,3,end_cells
        li      $v0,4
        la      $a0,char_bar
        syscall
        addi    $t0,$t0,4
        j       cell_loop

end_cells:
        li      $v0,4
        la      $a0,newline
        syscall
        addi    $t0,$t0,4
        addi    $t1,$t1,1
        blt     $t1,3,row_loop
        jr      $ra

# ----------------------------------------
# check_win: jeœli ktoœ wygra³ zwraca 1 (human)
# lub 2 (comp), w pp. 0
# ----------------------------------------
check_win:
        la      $t0,win_patterns
        li      $t1,8       # 8 wzorców
        li      $t2,1       # marker=1 (human)

# sprawdzamy dla human
chk_loop_h:
        beqz    $t1,chk_comp
        lw      $s0,0($t0)
        lw      $s1,4($t0)
        lw      $s2,8($t0)
        la      $s3,board

        sll     $s4,$s0,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_h
        sll     $s4,$s1,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_h
        sll     $s4,$s2,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_h
        # all trzy => human wygrywa
        li      $v0,1
        jr      $ra

next_h:
        addi    $t0,$t0,12
        addi    $t1,$t1,-1
        j       chk_loop_h

# teraz comp
chk_comp:
        la      $t0,win_patterns
        li      $t1,8
        li      $t2,2       # marker=2 (comp)

chk_loop_c:
        beqz    $t1,no_win
        lw      $s0,0($t0)
        lw      $s1,4($t0)
        lw      $s2,8($t0)
        la      $s3,board

        sll     $s4,$s0,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_c
        sll     $s4,$s1,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_c
        sll     $s4,$s2,2
        add     $s4,$s3,$s4
        lw      $s5,0($s4)
        bne     $s5,$t2,next_c
        # comp wygrywa
        li      $v0,2
        jr      $ra

next_c:
        addi    $t0,$t0,12
        addi    $t1,$t1,-1
        j       chk_loop_c

no_win:
        li      $v0,0
        jr      $ra

# ----------------------------------------
# board_full: 1 jeœli brak wolnych pól, 0 w pp.
# ----------------------------------------
board_full:
        la      $t0,board
        li      $t1,9

bf_loop:
        beqz    $t1,bf_full
        lw      $t2,0($t0)
        beqz    $t2,bf_not_full
        addi    $t0,$t0,4
        addi    $t1,$t1,-1
        j       bf_loop

bf_full:
        li      $v0,1
        jr      $ra

bf_not_full:
        li      $v0,0
        jr      $ra

# ----------------------------------------
# human_move: czyta ruch i zapisuje 1
# ----------------------------------------
human_move:
hm_loop:
        li      $v0,4
        la      $a0,prompt_move
        syscall

        li      $v0,5
        syscall
        move    $t0,$v0

        li      $t1,1
        blt     $t0,$t1,hm_inv
        li      $t1,9
        bgt     $t0,$t1,hm_inv

        addi    $t0,$t0,-1
        sll     $t2,$t0,2
        la      $t3,board
        add     $t3,$t3,$t2
        lw      $t4,0($t3)
        bne     $t4,$zero,hm_inv

        li      $t5,1
        sw      $t5,0($t3)
        jr      $ra

hm_inv:
        li      $v0,4
        la      $a0,invalid_move
        syscall
        j       hm_loop

# ----------------------------------------
# comp_move: najpierw œrodek, potem pierwsze
# wolne pole, zapisuje 2
# ----------------------------------------
comp_move:
        la      $t0,board
        lw      $t1,16($t0)    # board[4]
        bne     $t1,$zero,cm_scan
        li      $t2,2
        sw      $t2,16($t0)
        jr      $ra

cm_scan:
        li      $t3,0
cm_loop1:
        beq     $t3,9,cm_end
        sll     $t4,$t3,2
        la      $t5,board
        add     $t5,$t5,$t4
        lw      $t6,0($t5)
        bne     $t6,$zero,cm_next
        li      $t6,2
        sw      $t6,0($t5)
        jr      $ra

cm_next:
        addi    $t3,$t3,1
        j       cm_loop1

cm_end:
        jr      $ra