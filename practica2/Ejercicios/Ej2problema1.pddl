(define (problem Ej2problema1)
  (:domain Ejercicio2)
  (:objects z1 z2 z3 z4 z5 z6 z7 z8 z9 z10 z11 z12 z13 z14 z15 z16 z17 z18 z19 z20 z21 z22 z23 z24 z25 - zona
            jugador - player
            princesa principe bruja profesor leonardo - personajes
            oscar1 manzana1 rosa1 algoritmo1 oro1 - objetos)
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

    ; Primera fila
    (= (distancia z1 z6) 1)
    (= (distancia z2 z7) 1)
    (= (distancia z3 z8) 1)
    (= (distancia z4 z9) 1)
    (= (distancia z5 z10) 1)
    ; Segunda fila
    (= (distancia z6 z1) 1)
    (= (distancia z6 z7) 5)
    (= (distancia z6 z11) 10)
    (= (distancia z7 z2) 1)
    (= (distancia z7 z6) 5)
    (= (distancia z7 z8) 5)
    (= (distancia z8 z3) 1)
    (= (distancia z8 z7) 5)
    (= (distancia z9 z4) 1)
    (= (distancia z9 z14) 10)
    (= (distancia z10 z5) 1)
    (= (distancia z10 z15) 10)
    ; Tercera fila
    (= (distancia z11 z6) 10)
    (= (distancia z11 z12) 15)
    (= (distancia z11 z16) 20)
    (= (distancia z12 z11) 15)
    (= (distancia z12 z13) 15)
    (= (distancia z13 z12) 15)
    (= (distancia z13 z18) 20)
    (= (distancia z13 z14) 15)
    (= (distancia z14 z9) 10)
    (= (distancia z14 z13) 15)
    (= (distancia z14 z19) 20)
    (= (distancia z15 z10) 10)
    (= (distancia z15 z20) 20)
    ; Cuarta fila
    (= (distancia z16 z11) 20)
    (= (distancia z16 z21) 30)
    (= (distancia z17 z18) 25)
    (= (distancia z17 z22) 30)
    (= (distancia z18 z13) 20)
    (= (distancia z18 z17) 25)
    (= (distancia z18 z23) 30)
    (= (distancia z19 z14) 20)
    (= (distancia z19 z20) 25)
    (= (distancia z20 z15) 20)
    (= (distancia z20 z19) 25)
    (= (distancia z20 z25) 30)
    ; Quinta fila
    (= (distancia z21 z16) 30)
    (= (distancia z21 z22) 35)
    (= (distancia z22 z21) 35)
    (= (distancia z22 z17) 30)
    (= (distancia z22 z23) 35)
    (= (distancia z23 z22) 35)
    (= (distancia z23 z18) 30)
    (= (distancia z23 z24) 35)
    (= (distancia z24 z23) 35)
    (= (distancia z24 z25) 35)
    (= (distancia z25 z24) 35)
    (= (distancia z25 z20) 30)

    (= (distancia-total) 0)

    (posicion-player jugador z1)

    (posicion-personaje bruja z9)
    (posicion-personaje princesa z16)
    (posicion-personaje principe z21)
    (posicion-personaje profesor z20)
    (posicion-personaje leonardo z18)

    (posicion-objeto manzana1 z3)
    (posicion-objeto oscar1 z15)
    (posicion-objeto oro1 z24)
    (posicion-objeto algoritmo1 z22)
    (posicion-objeto rosa1 z11)

    (orientacion-player jugador norte)

    (manovacia jugador)
  )

  (:goal 
    (and (personaje-tiene leonardo) (personaje-tiene bruja) (personaje-tiene princesa)
         (personaje-tiene profesor) (personaje-tiene principe))
  )

  (:metric minimize (distancia-total))
 
 )