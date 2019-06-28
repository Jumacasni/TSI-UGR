(define (problem Ej1problema1)
    (:domain Ejercicio1)
  (:objects	z1 z2 z3 z4 z5 z6 z7 z8 z9 z10 z11 z12 z13 z14 z15 z16 z17 z18 z19 z20 z21 z22 z23 z24 z25 - zona
            jugador - player
            princesa principe bruja profesor leonardo - personajes
            oscar1 manzana1 manzana2 rosa1 algoritmo1 oro1 - objetos)
  
  (:init 
  	(orientacion-player jugador norte)
  )

  (:goal 
  	(and (orientacion-player jugador sur))
  )
)