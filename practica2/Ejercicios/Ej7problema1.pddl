(define (problem Ej7problema1)
    (:domain Ejercicio7)
  (:objects z1 z2 z3 z4 z5 z6 z7 z8 z9 z10 z11 z12 z13 z14 z15 z16 z17 z18 z19 z20 z21 z22 z23 z24 z25 - zona
            jugador1 - picker
            jugador2 - dealer
            princesa principe bruja profesor leonardo - personajes
            oscar1 manzana1 manzana2 rosa1 rosa2 algoritmo1 oro1 bikini1 bikini2 zapatillas1 - objetos)
  (:init 
  	; Primera fila
  	(conexion z1 z6 sur)
  	(conexion z2 z7 sur)
  	(conexion z3 z8 sur)
  	(conexion z4 z9 sur)
  	(conexion z5 z10 sur)
  	; Segunda fila
  	(conexion z6 z1 norte)
  	(conexion z6 z7 este)
  	(conexion z6 z11 sur)
  	(conexion z7 z2 norte)
  	(conexion z7 z6 oeste)
  	(conexion z7 z8 este)
  	(conexion z8 z3 norte)
  	(conexion z8 z7 oeste)
  	(conexion z9 z4 norte)
  	(conexion z9 z14 sur)
  	(conexion z10 z5 norte)
  	(conexion z10 z15 sur)
  	; Tercera fila
  	(conexion z11 z6 norte)
  	(conexion z11 z12 este)
  	(conexion z11 z16 sur)
  	(conexion z12 z11 oeste)
  	(conexion z12 z13 este)
  	(conexion z13 z12 oeste)
  	(conexion z13 z18 sur)
  	(conexion z13 z14 este)
  	(conexion z14 z9 norte)
  	(conexion z14 z13 oeste)
  	(conexion z14 z19 sur)
  	(conexion z15 z10 norte)
  	(conexion z15 z20 sur)
  	; Cuarta fila
  	(conexion z16 z11 norte)
  	(conexion z16 z21 sur)
  	(conexion z17 z18 este)
  	(conexion z17 z22 sur)
  	(conexion z18 z13 norte)
  	(conexion z18 z17 oeste)
  	(conexion z18 z23 sur)
  	(conexion z19 z14 norte)
  	(conexion z19 z20 este)
  	(conexion z20 z15 norte)
  	(conexion z20 z19 oeste)
  	(conexion z20 z25 sur)
  	; Quinta fila
  	(conexion z21 z16 norte)
  	(conexion z21 z22 este)
  	(conexion z22 z21 oeste)
  	(conexion z22 z17 norte)
  	(conexion z22 z23 este)
  	(conexion z23 z22 oeste)
  	(conexion z23 z18 norte)
  	(conexion z23 z24 este)
  	(conexion z24 z23 oeste)
  	(conexion z24 z25 este)
  	(conexion z25 z24 oeste)
  	(conexion z25 z20 norte)

    (zona z1 piedra)
    (zona z2 agua)
    (zona z3 bosque)
    (zona z4 bosque)
    (zona z5 bosque)
    (zona z6 piedra)
    (zona z7 piedra)
    (zona z8 bosque)
    (zona z9 bosque)
    (zona z10 bosque)
    (zona z11 piedra)
    (zona z12 piedra)
    (zona z13 piedra)
    (zona z14 bosque)
    (zona z15 bosque)
    (zona z16 bosque)
    (zona z17 bosque)
    (zona z18 bosque)
    (zona z19 bosque)
    (zona z20 bosque)
    (zona z21 bosque)
    (zona z22 bosque)
    (zona z23 bosque)
    (zona z24 bosque)
    (zona z25 bosque)

    (superficie-transitable Piedra)
    (superficie-transitable Arena)

    (superficie-transitable-si Agua bikini1)
    (superficie-transitable-si Agua bikini2)
    (superficie-transitable-si Bosque zapatillas1)

    (= (puntuacion oscar leonardo) 10)
    (= (puntuacion oscar princesa) 5)
    (= (puntuacion oscar bruja) 4)
    (= (puntuacion oscar profesor) 3)
    (= (puntuacion oscar principe) 1)

    (= (puntuacion rosa leonardo) 1)
    (= (puntuacion rosa princesa) 10)
    (= (puntuacion rosa bruja) 5)
    (= (puntuacion rosa profesor) 4)
    (= (puntuacion rosa principe) 3)

    (= (puntuacion manzana leonardo) 3)
    (= (puntuacion manzana princesa) 1)
    (= (puntuacion manzana bruja) 10)
    (= (puntuacion manzana profesor) 5)
    (= (puntuacion manzana principe) 4)

    (= (puntuacion algoritmo leonardo) 4)
    (= (puntuacion algoritmo princesa) 3)
    (= (puntuacion algoritmo bruja) 1)
    (= (puntuacion algoritmo profesor) 10)
    (= (puntuacion algoritmo principe) 5)

    (= (puntuacion oro leonardo) 5)
    (= (puntuacion oro princesa) 4)
    (= (puntuacion oro bruja) 3)
    (= (puntuacion oro profesor) 1)
    (= (puntuacion oro principe) 10)

    (= (puntuacion-total) 0)

    ; Primera fila
    (= (distancia z1 z6) 1)
    (= (distancia z2 z7) 1)
    (= (distancia z3 z8) 1)
    (= (distancia z4 z9) 1)
    (= (distancia z5 z10) 1)
    ; Segunda fila
    (= (distancia z6 z1) 1)
    (= (distancia z6 z7) 1)
    (= (distancia z6 z11) 1)
    (= (distancia z7 z2) 1)
    (= (distancia z7 z6) 1)
    (= (distancia z7 z8) 1)
    (= (distancia z8 z3) 1)
    (= (distancia z8 z7) 1)
    (= (distancia z9 z4) 1)
    (= (distancia z9 z14) 1)
    (= (distancia z10 z5) 1)
    (= (distancia z10 z15) 1)
    ; Tercera fila
    (= (distancia z11 z6) 1)
    (= (distancia z11 z12) 1)
    (= (distancia z11 z16) 1)
    (= (distancia z12 z11) 1)
    (= (distancia z12 z13) 1)
    (= (distancia z13 z12) 1)
    (= (distancia z13 z18) 1)
    (= (distancia z13 z14) 1)
    (= (distancia z14 z9) 1)
    (= (distancia z14 z13) 1)
    (= (distancia z14 z19) 1)
    (= (distancia z15 z10) 1)
    (= (distancia z15 z20) 1)
    ; Cuarta fila
    (= (distancia z16 z11) 1)
    (= (distancia z16 z21) 1)
    (= (distancia z17 z18) 1)
    (= (distancia z17 z22) 1)
    (= (distancia z18 z13) 1)
    (= (distancia z18 z17) 1)
    (= (distancia z18 z23) 1)
    (= (distancia z19 z14) 1)
    (= (distancia z19 z20) 1)
    (= (distancia z20 z15) 1)
    (= (distancia z20 z19) 1)
    (= (distancia z20 z25) 1)
    ; Quinta fila
    (= (distancia z21 z16) 1)
    (= (distancia z21 z22) 1)
    (= (distancia z22 z21) 1)
    (= (distancia z22 z17) 1)
    (= (distancia z22 z23) 1)
    (= (distancia z23 z22) 1)
    (= (distancia z23 z18) 1)
    (= (distancia z23 z24) 1)
    (= (distancia z24 z23) 1)
    (= (distancia z24 z25) 1)
    (= (distancia z25 z24) 1)
    (= (distancia z25 z20) 1)

    (= (distancia-total) 0)

    (= (capacidad-bolsillo leonardo) 0)
    (= (capacidad-bolsillo princesa) 5)
    (= (capacidad-bolsillo bruja) 4)
    (= (capacidad-bolsillo profesor) 0)
    (= (capacidad-bolsillo principe) 0)

    (= (bolsillo leonardo) 0)
    (= (bolsillo princesa) 0)
    (= (bolsillo bruja) 0)
    (= (bolsillo profesor) 0)
    (= (bolsillo principe) 0)

    (posicion-player jugador1 z1)
    (posicion-player jugador2 z1)

    (= (puntuacion-player jugador2) 0)

  	(posicion-personaje bruja z9)
  	(posicion-personaje princesa z16)
  	(posicion-personaje principe z21)
  	(posicion-personaje profesor z20)
  	(posicion-personaje leonardo z18)

  	(posicion-objeto manzana1 z3)
    (posicion-objeto manzana2 z4)
  	(posicion-objeto oscar1 z15)
  	(posicion-objeto oro1 z24)
  	(posicion-objeto algoritmo1 z22)
  	(posicion-objeto rosa1 z11)
    (posicion-objeto rosa2 z17)
  	(posicion-objeto zapatillas1 z2)
  	(posicion-objeto bikini1 z12)
  	(posicion-objeto bikini2 z14)

    (objeto-manzana manzana1)
    (objeto-manzana manzana2)
    (objeto-oscar oscar1)
    (objeto-rosa rosa1)
    (objeto-rosa rosa2)
    (objeto-oro oro1)
    (objeto-algoritmo algoritmo1)

  	(orientacion-player jugador1 norte)
    (orientacion-player jugador2 norte)

  	(manovacia jugador1)
    (manovacia jugador2)

    (mochilavacia jugador1)
    (mochilavacia jugador2)
  )

  (:goal 
  	(and (= (bolsillo bruja) 1))
  )
  
  (:metric minimize (distancia-total))
 )