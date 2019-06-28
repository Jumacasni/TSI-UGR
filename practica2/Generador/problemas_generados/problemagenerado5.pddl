(define (problem Problema5)
	(:domain Ejercicio5)
	(:objects z1 z6 z11 z16 z21 z2 z7 z3 z8 z4 z9 z14 z19 z5 z10 z15 z20 z25 z13 z18 z23 z17 z22 z12 z24 - zona
		player1 - player
		princesa1 principe1 bruja1 profesor1 leonardo1 - personajes
		zapatillas1 manzana1 manzana2 bikini2 oscar1 algoritmo1 bikini1 oro1 - objetos)
	(:init
		(conexion z1 z6 sur)
		(conexion z6 z1 norte)
		(conexion z6 z11 sur)
		(conexion z11 z6 norte)
		(conexion z11 z16 sur)
		(conexion z16 z11 norte)
		(conexion z16 z21 sur)
		(conexion z21 z16 norte)
		(conexion z2 z7 sur)
		(conexion z7 z2 norte)
		(conexion z3 z8 sur)
		(conexion z8 z3 norte)
		(conexion z4 z9 sur)
		(conexion z9 z4 norte)
		(conexion z9 z14 sur)
		(conexion z14 z9 norte)
		(conexion z14 z19 sur)
		(conexion z19 z14 norte)
		(conexion z5 z10 sur)
		(conexion z10 z5 norte)
		(conexion z10 z15 sur)
		(conexion z15 z10 norte)
		(conexion z15 z20 sur)
		(conexion z20 z15 norte)
		(conexion z20 z25 sur)
		(conexion z25 z20 norte)
		(conexion z13 z18 sur)
		(conexion z18 z13 norte)
		(conexion z18 z23 sur)
		(conexion z23 z18 norte)
		(conexion z17 z22 sur)
		(conexion z22 z17 norte)
		(conexion z6 z7 este)
		(conexion z7 z6 oeste)
		(conexion z7 z8 este)
		(conexion z8 z7 oeste)
		(conexion z11 z12 este)
		(conexion z12 z11 oeste)
		(conexion z12 z13 este)
		(conexion z13 z12 oeste)
		(conexion z13 z14 este)
		(conexion z14 z13 oeste)
		(conexion z17 z18 este)
		(conexion z18 z17 oeste)
		(conexion z19 z20 este)
		(conexion z20 z19 oeste)
		(conexion z21 z22 este)
		(conexion z22 z21 oeste)
		(conexion z22 z23 este)
		(conexion z23 z22 oeste)
		(conexion z23 z24 este)
		(conexion z24 z23 oeste)
		(conexion z24 z25 este)
		(conexion z25 z24 oeste)

		(zona z1 Piedra)
		(zona z6 Piedra)
		(zona z11 Piedra)
		(zona z16 Bosque)
		(zona z21 Bosque)
		(zona z2 Agua)
		(zona z7 Piedra)
		(zona z3 Bosque)
		(zona z8 Bosque)
		(zona z4 Bosque)
		(zona z9 Bosque)
		(zona z14 Bosque)
		(zona z19 Bosque)
		(zona z5 Bosque)
		(zona z10 Bosque)
		(zona z15 Bosque)
		(zona z20 Bosque)
		(zona z25 Bosque)
		(zona z13 Piedra)
		(zona z18 Bosque)
		(zona z23 Bosque)
		(zona z17 Bosque)
		(zona z22 Bosque)
		(zona z12 Piedra)
		(zona z24 Bosque)

		(superficie-transitable Piedra)
		(superficie-transitable Arena)
		(superficie-transitable-si Agua bikini2)
		(superficie-transitable-si Agua bikini1)
		(superficie-transitable-si Bosque zapatillas1)

		(= (distancia z1 z6) 1)
		(= (distancia z6 z1) 1)
		(= (distancia z6 z11) 1)
		(= (distancia z11 z6) 1)
		(= (distancia z11 z16) 1)
		(= (distancia z16 z11) 1)
		(= (distancia z16 z21) 1)
		(= (distancia z21 z16) 1)
		(= (distancia z2 z7) 1)
		(= (distancia z7 z2) 1)
		(= (distancia z3 z8) 1)
		(= (distancia z8 z3) 1)
		(= (distancia z4 z9) 1)
		(= (distancia z9 z4) 1)
		(= (distancia z9 z14) 1)
		(= (distancia z14 z9) 1)
		(= (distancia z14 z19) 1)
		(= (distancia z19 z14) 1)
		(= (distancia z5 z10) 1)
		(= (distancia z10 z5) 1)
		(= (distancia z10 z15) 1)
		(= (distancia z15 z10) 1)
		(= (distancia z15 z20) 1)
		(= (distancia z20 z15) 1)
		(= (distancia z20 z25) 1)
		(= (distancia z25 z20) 1)
		(= (distancia z13 z18) 1)
		(= (distancia z18 z13) 1)
		(= (distancia z18 z23) 1)
		(= (distancia z23 z18) 1)
		(= (distancia z17 z22) 1)
		(= (distancia z22 z17) 1)
		(= (distancia z6 z7) 1)
		(= (distancia z7 z6) 1)
		(= (distancia z7 z8) 1)
		(= (distancia z8 z7) 1)
		(= (distancia z11 z12) 1)
		(= (distancia z12 z11) 1)
		(= (distancia z12 z13) 1)
		(= (distancia z13 z12) 1)
		(= (distancia z13 z14) 1)
		(= (distancia z14 z13) 1)
		(= (distancia z17 z18) 1)
		(= (distancia z18 z17) 1)
		(= (distancia z19 z20) 1)
		(= (distancia z20 z19) 1)
		(= (distancia z21 z22) 1)
		(= (distancia z22 z21) 1)
		(= (distancia z22 z23) 1)
		(= (distancia z23 z22) 1)
		(= (distancia z23 z24) 1)
		(= (distancia z24 z23) 1)
		(= (distancia z24 z25) 1)
		(= (distancia z25 z24) 1)

		(= (distancia-total) 0)

		(= (capacidad-bolsillo princesa1) 5)
		(= (capacidad-bolsillo bruja1) 2)
		(= (capacidad-bolsillo profesor1) 0)
		(= (capacidad-bolsillo principe1) 0)
		(= (capacidad-bolsillo leonardo1) 0)

		(= (bolsillo princesa1) 0)
		(= (bolsillo bruja1) 0)
		(= (bolsillo profesor1) 0)
		(= (bolsillo principe1) 0)
		(= (bolsillo leonardo1) 0)

		(= (puntuacion manzana bruja1) 10)
		(= (puntuacion oscar bruja1) 4)
		(= (puntuacion rosa bruja1) 5)
		(= (puntuacion algoritmo bruja1) 1)
		(= (puntuacion oro bruja1) 3)
		(= (puntuacion manzana leonardo1) 3)
		(= (puntuacion oscar leonardo1) 10)
		(= (puntuacion rosa leonardo1) 1)
		(= (puntuacion algoritmo leonardo1) 4)
		(= (puntuacion oro leonardo1) 5)
		(= (puntuacion manzana princesa1) 1)
		(= (puntuacion oscar princesa1) 5)
		(= (puntuacion rosa princesa1) 10)
		(= (puntuacion algoritmo princesa1) 3)
		(= (puntuacion oro princesa1) 4)
		(= (puntuacion manzana profesor1) 5)
		(= (puntuacion oscar profesor1) 3)
		(= (puntuacion rosa profesor1) 4)
		(= (puntuacion algoritmo profesor1) 10)
		(= (puntuacion oro profesor1) 1)
		(= (puntuacion manzana principe1) 4)
		(= (puntuacion oscar principe1) 1)
		(= (puntuacion rosa principe1) 3)
		(= (puntuacion algoritmo principe1) 5)
		(= (puntuacion oro principe1) 10)

		(= (puntuacion-total) 0)

		(posicion-player player1 z1)

		(posicion-personaje princesa1 z16)
		(posicion-personaje principe1 z21)
		(posicion-personaje bruja1 z9)
		(posicion-personaje profesor1 z20)
		(posicion-personaje leonardo1 z18)

		(posicion-objeto zapatillas1 z2)
		(posicion-objeto manzana1 z3)
		(posicion-objeto manzana2 z4)
		(posicion-objeto bikini2 z14)
		(posicion-objeto oscar1 z15)
		(posicion-objeto algoritmo1 z22)
		(posicion-objeto bikini1 z12)
		(posicion-objeto oro1 z24)

		(objeto-manzana manzana1)
		(objeto-manzana manzana2)
		(objeto-oscar oscar1)
		(objeto-algoritmo algoritmo1)
		(objeto-oro oro1)

		(orientacion-player player1 norte)

		(manovacia player1)

		(mochilavacia player1)

	)

	(:goal
		(and (>= (puntuacion-total) 20))
	)

	(:metric minimize (distancia-total))
)