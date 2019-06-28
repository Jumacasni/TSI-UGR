(define (domain zeno-travel)


(:requirements
  :typing
  :fluents
  :derived-predicates
  :negative-preconditions
  :universal-preconditions
  :disjuntive-preconditions
  :conditional-effects
  :htn-expansion

  ; Requisitos adicionales para el manejo del tiempo
  :durative-actions
  :metatags
 )

(:types aircraft person city - object)
(:constants slow fast - object)
(:predicates (at ?x - (either person aircraft) ?c - city)
             (in ?p - person ?a - aircraft)
             (different ?x ?y) (igual ?x ?y)
             (destino ?x - person ?y - city)
             (hay-fuel-fast-speed ?a ?c1 ?c2)
             (hay-fuel-slow-speed ?a ?c1 ?c2)
             (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2)
             (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2)
             (no-supera-duration-limit-fast-speed ?a - aircraft ?c1 - city ?c2 - city)  ;; NUEVO PREDICADO PARA EL PROBLEMA 4
             (no-supera-duration-limit-slow-speed ?a - aircraft ?c1 - city ?c2 - city)  ;; NUEVO PREDICADO PARA EL PROBLEMA 4
             )
(:functions (fuel ?a - aircraft)
            (distance ?c1 - city ?c2 - city)
            (fuel-limit ?a - aircraft)
            (capacidad-maxima ?a - aircraft)        ;; FUNCIÓN PARA EL PROBLEMA 4
            (numero-de-pasajeros ?a - aircraft)     ;; FUNCIÓN PARA EL PROBLEMA 4
            (slow-speed ?a - aircraft)
            (fast-speed ?a - aircraft)
            (slow-burn ?a - aircraft)
            (fast-burn ?a - aircraft)
            (capacity ?a - aircraft)
            (refuel-rate ?a - aircraft)
            (total-fuel-used ?a - aircraft)
            (boarding-time)
            (debarking-time)
            (duration-limit ?a - aircraft)
            )

;; el consecuente "vac�o" se representa como "()" y significa "siempre verdad"
(:derived
  (igual ?x ?x) ())

(:derived
  (different ?x ?y) (not (igual ?x ?y)))

(:derived
    (hay-fuel-fast-speed ?a - aircraft ?c1 - city ?c2 - city)
    (>= (fuel ?a) (* (distance ?c1 ?c2) (fast-burn ?a))))

(:derived
    (hay-fuel-slow-speed ?a - aircraft ?c1 - city ?c2 - city)
    (>= (fuel ?a) (* (distance ?c1 ?c2) (slow-burn ?a))))

(:derived
    (no-supera-fuel-limit-fast-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (+ (total-fuel-used ?a) (* (distance ?c1 ?c2) (fast-burn ?a))) (fuel-limit ?a)))

(:derived
    (no-supera-fuel-limit-slow-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (+ (total-fuel-used ?a) (* (distance ?c1 ?c2) (slow-burn ?a))) (fuel-limit ?a)))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si la duración del viaje desde ?c1 a ?c2 a VELOCIDAD RÁPIDA no supera la duración límite para el avión ?a
(:derived
    (no-supera-duration-limit-fast-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (/ (distance ?c1 ?c2) (fast-speed ?a)) (duration-limit ?a)))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si la duración del viaje desde ?c1 a ?c2 a VELOCIDAD LENTA no supera la duración límite para el avión ?a
(:derived
    (no-supera-duration-limit-slow-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (/ (distance ?c1 ?c2) (slow-speed ?a)) (duration-limit ?a)))

(:task transport-person
	:parameters (?p - person ?c - city)

  (:method Case1 ; si la persona est� en la ciudad no se hace nada
	 :precondition (at ?p ?c)
	 :tasks ()
   )


   (:method Case2 ;si no está en la ciudad destino, pero avion y persona están en la misma ciudad
	  :precondition (and (at ?p - person ?c1 - city)
			                 (at ?a - aircraft ?c1 - city)
                             (< (numero-de-pasajeros ?a) (capacidad-maxima ?a)))

	  :tasks (
	  	        (board-recursivo ?a ?c1)
		        (mover-avion ?a ?c1 ?c)
		        (debark-recursivo ?a ?c)))

    (:method Case3 ;si no está en la ciudad destino, y avión y persona están en distinta ciudad
        :precondition (and (at ?p - person ?c1 - city)
			                 (at ?a - aircraft ?c2 - city)
                             (different ?c1 ?c2)
                             (< (numero-de-pasajeros ?a) (capacidad-maxima ?a)))

    	  :tasks (
    		        (mover-avion ?a ?c2 ?c1)
                    (board-recursivo ?a ?c1)
                    (mover-avion ?a ?c1 ?c)
                    (debark-recursivo ?a ?c)
                )
    )
)

(:task mover-avion
    :parameters (?a - aircraft ?c1 - city ?c2 -city)

    (:method fuel-suficiente-fast-speed

        :precondition (and
                        (hay-fuel-fast-speed ?a ?c1 ?c2)
                        (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2)
                        (no-supera-duration-limit-fast-speed ?a ?c1 ?c2))
        :tasks (
                (zoom ?a ?c1 ?c2)
               )
    )

    (:method fuel-insuficiente-fast-speed

        :precondition (and
                        (not (hay-fuel-fast-speed ?a ?c1 ?c2))
                        (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2)
                        (no-supera-duration-limit-fast-speed ?a ?c1 ?c2))
        :tasks (
                (refuel ?a ?c1)
                (zoom ?a ?c1 ?c2)
               )
    )

    (:method fuel-suficiente-slow-speed

        :precondition (and
                        (hay-fuel-slow-speed ?a ?c1 ?c2)
                        (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2)
                        (no-supera-duration-limit-slow-speed ?a ?c1 ?c2))
        :tasks (
                (fly ?a ?c1 ?c2)
               )
    )

    (:method fuel-insuficiente-slow-speed

        :precondition (and
                        (not (hay-fuel-slow-speed ?a ?c1 ?c2))
                        (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2)
                        (no-supera-duration-limit-slow-speed ?a ?c1 ?c2))
        :tasks (
                (refuel ?a ?c1)
                (fly ?a ?c1 ?c2)
               )
    )
)

; /*
; * TAREA PARA EMBARCAR RECURSIVAMENTE QUE SE AÑADE PARA EL PROBLEMA 4
; */
(:task board-recursivo
	:parameters (?a - aircraft ?c - city)
	(:method recursion
		:precondition (and  (at ?p ?c) (at ?a ?c) (< (numero-de-pasajeros ?a) (capacidad-maxima ?a)) (not (destino ?p ?c)))
		:tasks  (
                 (board ?p ?a ?c)
                 (board-recursivo ?a ?c)
		        )
	)
	(:method caso-base
	:precondition():tasks()
	)
)

; /*
; * TAREA PARA DESEMBARCAR RECURSIVAMENTE QUE SE AÑADE PARA EL PROBLEMA 4
; */
(:task debark-recursivo
	:parameters (?a - aircraft ?c - city)
	(:method recursion
		:precondition (and (in ?p ?a) (at ?a ?c))
		:tasks  (
                 (debark ?p ?a ?c)
                 (debark-recursivo ?a ?c)
                )
	)
	(:method caso-base
	:precondition():tasks())
)

(:import "Primitivas-Zenotravel-E4.pddl")


)
