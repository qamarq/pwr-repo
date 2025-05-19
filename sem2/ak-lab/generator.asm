.data
    alphanumeric_chars: .asciiz "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    # D³ugoœæ powy¿szego ci¹gu to 10 + 26 + 26 = 62
    len_alphanumeric:   .word 62
    newline:            .asciiz "\n"
    buffer:             .space 11  # 10 znaków + null terminator

.text
.globl main

main:
    # Inicjalizacja generatora liczb losowych (PRNG)
    # 1. Pobierz aktualny czas
    li $v0, 30          # syscall code for time
    syscall             # Czas (dolne 32 bity) w $a0, (górne 32 bity) w $a1
                        # U¿yjemy $a0 jako ziarna

    # 2. Ustaw ziarno dla PRNG
    # $a0 ju¿ zawiera czas, wiêc jest gotowe jako argument dla srandom
    li $v0, 40          # syscall code for srandom
    syscall

    # Licznik zewnêtrznej pêtli (ile ci¹gów wygenerowaæ)
    li $t0, 0           # i = 0 (licznik ci¹gów)
    li $t7, 10          # Maksymalna liczba ci¹gów

outer_loop:
    beq $t0, $t7, exit_program # Jeœli i == 10, zakoñcz

    # Przygotowanie do generowania pojedynczego ci¹gu
    la $s0, buffer      # $s0 wskaŸnik na pocz¹tek bufora
    li $t1, 0           # j = 0 (licznik znaków w bie¿¹cym ci¹gu)
    li $t8, 10          # Maksymalna liczba znaków w ci¹gu

inner_loop:
    beq $t1, $t8, string_done # Jeœli j == 10, ci¹g jest gotowy

    # Generuj losowy indeks
    # $a0 = 0 (dolna granica dla random_int_range) - nie trzeba ustawiaæ, jeœli jest 0
    # $a1 = górna granica (wy³¹cznie)
    lw $a1, len_alphanumeric # $a1 = 62
    li $v0, 42          # syscall code for random_int_range
    syscall             # Losowy indeks w $a0 (0 do 61)

    # Pobierz znak z alphanumeric_chars
    la $t2, alphanumeric_chars # $t2 = adres bazowy alphanumeric_chars
    addu $t3, $t2, $a0  # $t3 = adres znaku (baza + indeks)
    lb $t4, 0($t3)      # $t4 = wczytany znak (bajt)

    # Zapisz znak w buforze
    sb $t4, 0($s0)      # Zapisz znak do bufora pod adresem w $s0
    addi $s0, $s0, 1    # Przesuñ wskaŸnik bufora o 1 bajt

    addi $t1, $t1, 1    # j++
    j inner_loop

string_done:
    # Dodaj null terminator na koñcu ci¹gu w buforze
    sb $zero, 0($s0)    # Zapisz bajt zerowy (null)

    # Wydrukuj wygenerowany ci¹g
    la $a0, buffer
    li $v0, 4           # syscall code for print_string
    syscall

    # Wydrukuj znak nowej linii
    la $a0, newline
    li $v0, 4           # syscall code for print_string
    syscall

    addi $t0, $t0, 1    # i++
    j outer_loop

exit_program:
    li $v0, 10          # syscall code for exit
    syscall