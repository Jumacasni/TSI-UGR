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
             (hay-fuel-fast-speed ?a ?c1 ?c2)                   ;; NUEVO PREDICADO PARA EL PROBLEMA 3
             (hay-fuel-slow-speed ?a ?c1 ?c2)                   ;; NUEVO PREDICADO PARA EL PROBLEMA 3
             (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2)       ;; NUEVO PREDICADO PARA EL PROBLEMA 3
             (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2)       ;; NUEVO PREDICADO PARA EL PROBLEMA 3
             )
(:functions (fuel ?a - aircraft)
            (distance ?c1 - city ?c2 - city)
            (fuel-limit)                       ;; NUEVA FUNCIÓN PARA EL PROBLEMA 3: limite de fuel
            (slow-speed ?a - aircraft)
            (fast-speed ?a - aircraft)
            (slow-burn ?a - aircraft)
            (fast-burn ?a - aircraft)
            (capacity ?a - aircraft)
            (refuel-rate ?a - aircraft)
            (total-fuel-used)
            (boarding-time)
            (debarking-time)
            )

;; el consecuente "vac�o" se representa como "()" y significa "siempre verdad"
(:derived
  (igual ?x ?x) ())

(:derived
  (different ?x ?y) (not (igual ?x ?y)))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si hay fuel suficiente para que el avión ?a vuele de la ciudad ?c1 a la ?c2 a VELOCIDAD RÁPIDA
(:derived
    (hay-fuel-fast-speed ?a - aircraft ?c1 - city ?c2 - city)
    (>= (fuel ?a) (* (distance ?c1 ?c2) (fast-burn ?a))))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si hay fuel suficiente para que el avión ?a vuele de la ciudad ?c1 a la ?c2 a VELOCIDAD LENTA
(:derived
    (hay-fuel-slow-speed ?a - aircraft ?c1 - city ?c2 - city)
    (>= (fuel ?a) (* (distance ?c1 ?c2) (slow-burn ?a))))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si no se supera el límite de fuel para ir desde ?c1 hasta ?c2 a VELOCIDAD RÁPIDA
(:derived
    (no-supera-fuel-limit-fast-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (+ (total-fuel-used) (* (distance ?c1 ?c2) (fast-burn ?a))) (fuel-limit)))

;; este literal derivado se utiliza para deducir, a partir de la información en el estado actual,
;; si no se supera el límite de fuel para ir desde ?c1 hasta ?c2 a VELOCIDAD LENTA
(:derived
    (no-supera-fuel-limit-slow-speed ?a - aircraft ?c1 - city ?c2 - city)
    (< (+ (total-fuel-used) (* (distance ?c1 ?c2) (slow-burn ?a))) (fuel-limit)))

(:task transport-person
	:parameters (?p - person ?c - city)

  (:method Case1 ; si la persona est� en la ciudad no se hace nada
	 :precondition (at ?p ?c)
	 :tasks ()
   )


   (:method Case2 ;si no está en la ciudad destino, pero avion y persona están en la misma ciudad
	  :precondition (and (at ?p - person ?c1 - city)
			                 (at ?a - aircraft ?c1 - city))

	  :tasks (
	  	        (board ?p ?a ?c1)
		        (mover-avion ?a ?c1 ?c)
		        (debark ?p ?a ?c )))

    (:method Case3 ;si no está en la ciudad destino, y avión y persona están en distinta ciudad
        :precondition (and (at ?p - person ?c1 - city)
			                 (at ?a - aircraft ?c2 - city)
                             (different ?c1 ?c2))

    	  :tasks (
    		        (mover-avion ?a ?c2 ?c1)
                    (board ?p ?a ?c1)
                    (mover-avion ?a ?c1 ?c)
                    (debark ?p ?a ?c)
                )
    )
)

(:task mover-avion
    :parameters (?a - aircraft ?c1 - city ?c2 -city)

    (:method fuel-suficiente-fast-speed ;; este método se escogerá para usar la acción zoom cuando el avión tenga fuel
                                      ;; para volar desde ?c1 a ?c2 a VELOCIDAD RÁPIDA
        :precondition (and (hay-fuel-fast-speed ?a ?c1 ?c2) (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2))
        :tasks (
                (zoom ?a ?c1 ?c2)
               )
    )

    (:method fuel-insuficiente-fast-speed ;; este método se escogerá para usar la acción zoom cuando el avión no tenga fuel
                                        ;; para volar desde ?c1 a ?c2 a VELOCIDAD RÁPIDA
        :precondition (and (not (hay-fuel-fast-speed ?a ?c1 ?c2)) (no-supera-fuel-limit-fast-speed ?a ?c1 ?c2))
        :tasks (
                (refuel ?a ?c1)
                (zoom ?a ?c1 ?c2)
               )
    )

    (:method fuel-suficiente-slow-speed ;; este método se escogerá para usar la acción fly cuando el avión tenga fuel
                                      ;; para volar desde ?c1 a ?c2 a VELOCIDAD LENTA
        :precondition (and (hay-fuel-slow-speed ?a ?c1 ?c2) (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2))
        :tasks (
                (fly ?a ?c1 ?c2)
               )
    )

    (:method fuel-insuficiente-slow-speed ;; este método se escogerá para usar la acción fly cuando el avión no tenga fuel
                                        ;; para volar desde ?c1 a ?c2 a VELOCIDAD LENTA
        :precondition (and (not (hay-fuel-slow-speed ?a ?c1 ?c2)) (no-supera-fuel-limit-slow-speed ?a ?c1 ?c2))
        :tasks (
                (refuel ?a ?c1)
                (fly ?a ?c1 ?c2)
               )
    )
)

(:import "Primitivas-Zenotravel.pddl")


)
