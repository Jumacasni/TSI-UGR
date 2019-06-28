(define (domain Ejercicio1)
  	(:requirements :strips :equality :typing)
  	(:types zona player personajes objetos)

  	(:constants 
  		norte sur este oeste - orientacion
 	)
 
  	(:predicates 
	    (conexion ?z1 - zona ?z2 - zona ?o - orientacion )
	    (posicion-player ?p - player ?z - zona)
	    (posicion-personaje ?p - personajes ?z - zona)
	    (posicion-objeto ?p - objetos ?z - zona)
	    (orientacion-player ?p - player ?o - orientacion)
	    (player-tiene ?p - player ?o - objetos)
	    (personaje-tiene ?p - personajes)
	    (personaje-tiene-objeto ?p - personajes ?o - objetos)
	    (manovacia ?p - player))

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
	    :parameters (?p - player ?orientacion - orientacion ?z1 - zona ?z2 - zona)
	    :precondition (and (conexion ?z1 ?z2 ?orientacion) (posicion-player ?p ?z1) (not (posicion-player ?p ?z2)) (orientacion-player ?p ?orientacion))
	    :effect (and
	     			(posicion-player ?p ?z2)
	     			(not (posicion-player ?p ?z1))
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
	    :parameters (?p - player ?object - objetos ?z - zona)
	    :precondition (and (player-tiene ?p ?object) (posicion-player ?p ?z))
	    :effect (and
	     			(posicion-objeto ?object ?z)
	     			(not (player-tiene ?p ?object))
	     			(manovacia ?p)
	     		 )
	)

	(:action GIVE
	    :parameters (?p - player ?object - objetos ?z - zona ?personaje - personajes)
	    :precondition (and (player-tiene ?p ?object) (posicion-player ?p ?z) (posicion-personaje ?personaje ?z))
	    :effect (and
	     			(personaje-tiene ?personaje)
	     			(not (player-tiene ?p ?object))
	     			(manovacia ?p)
	     		 )
	)
)
