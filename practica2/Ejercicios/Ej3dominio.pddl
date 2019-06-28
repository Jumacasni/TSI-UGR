(define (domain Ejercicio3)
  	(:requirements :strips :equality :typing :fluents)
  	(:types zona superficie player personajes objetos orientacion)
 
  	(:predicates 
	    (conexion ?z1 - zona ?z2 - zona ?o - orientacion )
	    (zona ?z1 - zona ?s - superficie)
	    (posicion-player ?p - player ?z - zona)
	    (posicion-personaje ?p - personajes ?z - zona)
	    (posicion-objeto ?p - objetos ?z - zona)
	    (orientacion-player ?p - player ?o - orientacion)
	    (player-tiene ?p - player ?o - objetos)
	    (personaje-tiene ?p - personajes)
	    (personaje-tiene-objeto ?p - personajes ?o - objetos)
	    (manovacia ?p - player)
	    (mochilavacia ?p - player)
	    (mochilacontiene ?p - player ?o - objetos)
	    (superficie-transitable ?s - superficie)
	    (superficie-transitable-si ?s - superficie ?o - objetos)
	    (objeto-manzana ?o - objetos)
	    (objeto-oro ?o - objetos)
	    (objeto-algoritmo ?o - objetos)
	    (objeto-rosa ?o - objetos)
	    (objeto-oscar ?o - objetos))

  	(:constants 
  		norte sur este oeste - orientacion
  		Bosque Agua Precipicio Arena Piedra - superficie
 	)
 	
  	(:functions
  		(distancia ?x ?y - zona)
  		(distancia-total)
  	)

  	(:action GIRAR-IZQUIERDA
	    :parameters (?p - player ?orientacion - orientacion)
	    :precondition (and (orientacion-player ?p ?orientacion))
	    :effect (and
	     			(when (and (orientacion-player ?p norte ))
	     				  (and (orientacion-player ?p oeste) (not (orientacion-player ?p norte)))
	     			)

	     			(when (and (orientacion-player ?p sur ))
	     				  (and (orientacion-player ?p este) (not (orientacion-player ?p sur)))
	     			)

	     			(when (and (orientacion-player ?p este ))
	     				  (and (orientacion-player ?p norte) (not (orientacion-player ?p este)))
	     			)

	     			(when (and (orientacion-player ?p oeste ))
	     				  (and (orientacion-player ?p sur) (not (orientacion-player ?p oeste)))
	     			)
	     		 )
	)

	(:action GIRAR-DERECHA
	    :parameters (?p - player ?orientacion - orientacion)
	    :precondition (and (orientacion-player ?p ?orientacion))
	    :effect (and
	     			(when (and (orientacion-player ?p norte ))
	     				  (and (orientacion-player ?p este) (not (orientacion-player ?p norte)))
	     			)

	     			(when (and (orientacion-player ?p sur ))
	     				  (and (orientacion-player ?p oeste) (not (orientacion-player ?p sur)))
	     			)

	     			(when (and (orientacion-player ?p este ))
	     				  (and (orientacion-player ?p sur) (not (orientacion-player ?p este)))
	     			)

	     			(when (and (orientacion-player ?p oeste ))
	     				  (and (orientacion-player ?p norte) (not (orientacion-player ?p oeste)))
	     			)
	     		 )
	)

	(:action GO-TO
	    :parameters (?p - player ?orientacion - orientacion ?z1 - zona ?z2 - zona ?s - superficie ?o - objetos)
	    :precondition (and (conexion ?z1 ?z2 ?orientacion) (posicion-player ?p ?z1) (not (posicion-player ?p ?z2)) (orientacion-player ?p ?orientacion)
	    			  (zona ?z2 ?s) (or (and (superficie-transitable ?s)) (and (or (player-tiene ?p ?o) (mochilacontiene ?p ?o)) (superficie-transitable-si ?s ?o))))
	    :effect (and
	     			(posicion-player ?p ?z2)
	     			(not (posicion-player ?p ?z1))
	     			(increase (distancia-total) (distancia ?z1 ?z2))
	     		 )
	)

	(:action PICK-UP
	    :parameters (?p - player ?object - objetos ?z - zona)
	    :precondition (and (posicion-player ?p ?z) (posicion-objeto ?object ?z) (manovacia ?p))
	    :effect (and
	     			(player-tiene ?p ?object)
	     			(not (posicion-objeto ?object ?z))
	     			(not (manovacia ?p))
	     		 )
	)

	(:action DROP
	    :parameters (?p - player ?object - objetos ?z - zona ?s - superficie)
	    :precondition (and (player-tiene ?p ?object) (posicion-player ?p ?z))
	    :effect (and
	     			(posicion-objeto ?object ?z)
	     			(not (player-tiene ?p ?object))
	     			(manovacia ?p)
	     		 )
	)

	(:action GIVE
	    :parameters (?p - player ?object - objetos ?z - zona ?personaje - personajes)
	    :precondition (and (player-tiene ?p ?object) (posicion-player ?p ?z) (posicion-personaje ?personaje ?z)
	    			  (or (objeto-manzana ?object) (objeto-oro ?object) (objeto-algoritmo ?object) (objeto-rosa ?object) (objeto-oscar ?object)))
	    :effect (and
	     			(personaje-tiene ?personaje)
	     			(not (player-tiene ?p ?object))
	     			(manovacia ?p)
	     		 )
	)

	(:action PUT-OBJECT
	    :parameters (?p - player ?o - objetos)
	    :precondition (and (player-tiene ?p ?o) (mochilavacia ?p))
	    :effect (and
	     			(not (player-tiene ?p ?o))
	     			(manovacia ?p)
	     			(not (mochilavacia ?p))
	     			(mochilacontiene ?p ?o)
	    		 )
	)

	(:action TAKE-OBJECT
	    :parameters (?p - player ?o - objetos)
	    :precondition (and (mochilacontiene ?p ?o) (manovacia ?p))
	    :effect (and
	     			(player-tiene ?p ?o)
	     			(not (manovacia ?p))
	     			(mochilavacia ?p)
	     			(not (mochilacontiene ?p ?o))
	     		 )
	)
)
