(define (domain Karel_domain)
(:requirements :strips)
(:predicates (at ?x ) 
             (allownorth ?x )
             (allowsouth ?x )
             (alloweast ?x )
             (allowwest ?x )
			 (north ?x ?y)
			 (south ?x ?y)
			 (east ?x ?y)
			 (west ?x ?y))
	
(:action MoveNorth
:parameters (?x ?y) 
:precondition (and (at ?x) (north ?y ?x) (allownorth ?x) )
:effect (and (not (at ?x)) (at ?y)))

(:action MoveSouth
:parameters (?x ?y) 
:precondition (and (at ?x) (south ?y ?x) (allowsouth ?x) )
:effect (and (not (at ?x)) (at ?y)))

(:action MoveEast
:parameters (?x ?y) 
:precondition (and (at ?x) (east ?y ?x) (alloweast ?x) )
:effect (and (not (at ?x)) (at ?y)))

(:action MoveWest
:parameters (?x ?y) 
:precondition (and (at ?x) (west ?y ?x) (allowwest ?x) )
:effect (and (not (at ?x)) (at ?y)))
)
