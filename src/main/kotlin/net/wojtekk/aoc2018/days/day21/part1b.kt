//#ip 5

fun main() {
    for (i in 0..Int.MAX_VALUE) {
        var a = 0
        var b = 0
        var c = 0
        var d = 0
        var e = 0
        var f = 0

        b = 123; f = 1 // #0. seti 123 0 1
        b = 456 and b; f = 2 // #1. bani 1 456 1
        b = if (b == 72) 1 else 0; f = 3 // #2. eqri 1 72 1
        f += b; f = 5 // JMP -> #5 // #3. addr 1 5 5
            f = 0 // JMP -> 0 // #4. seti 0 0 5
        b = 0; f = 6 // #5. seti 0 0 1
        c = b or 65536; f=7 // #6. bori 1 65536 2
        b = 6663054; f=8 // #7. seti 6663054 1 1
        e = c and 255; f=9 // #8. bani 2 255 4

        b = b + e; f=10 // #9. addr 1 4 1
        b = b and 16777215; f=11 // #10. bani 1 16777215 1
        b = b * 65899; f=12 // !!! // #11. muli 1 65899 1
        b = b and 16777215; f=13 // #12. bani 1 16777215 1

        e = if (256 > c) 1 else 0; f=14 // #13. gtir 256 2 4
        f = e + f + 1 // #14. addr 4 5 5
        f = f + 1 + 1 // JMP -> 17 // #15. addi 5 1 5
        f = 27 // JMP -> 27 // #16. seti 27 6 5
        e = 0; f=18 // #17. seti 0 6 4

        d = e + 1; f=19 // #18. addi 4 1 3
        d =  d * 256; f=20 // #19 muli 3 256 3
        d = if (d > c) 1 else 0; f=21 // #20. gtrr 3 2 3
        f = 22 // d + f // JMP // #21. addr 3 5 5

        f = 24 // f + 1 // JMP -> 24 // #22. addi 5 1 5
        f = 25 // JMP // #23. seti 25 9 5
        e = e + 1; f=25 // #24. addi 4 1 4
        f = 18 // JMP // #25. seti 17 3 5

        c = e // #26. setr 4 4 2
        f = 8 // JMP // #27. seti 7 2 5

        e = if (b == a) 1 else 0 // #28. eqrr 1 0 4 HERE !!!!!!!!!!
        f = e + f // JMP //#29. addr 4 5
        f = 5 // JMP // #30. seti 5 8 5
    }
}
