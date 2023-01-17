composite(1).

prime(2).
prime(N) :-
    N > 2,
    not(composite(N)).

init(MAX_N) :-
    \+ erot(2, MAX_N).

% fill table
filling_table(Current, F, Start_value) :-
    Current1 is Current + Start_value,
    Current1 =< F,
    assert(composite(Current1)),
    filling_table(Current1, F, Start_value).



% main func
erot(S, F) :-
    S =< F,
    \+ composite(S),
    filling_table(S, F, S).

% recursion
erot(S, F) :-
    S =< F,
    S1 is S + 1,
    erot(S1, F).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).


unique_prime_divisors(1, []).
unique_prime_divisors(N, Divisors) :- range_unic(2, N, Divisors, N, 0).


range_unic(L, L, [L], N, M) :- prime(L), !.
range_unic(L, L, [], 1, M) :- !.


range_unic(N, L, Divisors, R, M) :-
        N =< L,
    	prime(N),
    	0 =:= mod(R, N),
    	R1 is div(R, N),
    	\+ M =:= N,
      	concat([N], LowDiv, Divisors),
        range_unic(N, L, LowDiv, R1, N).

range_unic(N, L, Divisors, R, M) :-
        N =< L,
    	prime(N),
    	0 =:= mod(R, N),
    	R1 is div(R, N),
    	M =:= N,
      	range_unic(N, L, Divisors, R1, N).


range_unic(N, L, Divisors, R, M) :-
    N =< L,
    N1 is N + 1,
    range_unic(N1, L, Divisors, R, M).








prime_divisors(1, []).
prime_divisors(N, Divisors) :- range_prime(2, N, Divisors, N).
range_prime(L, L, [L], N) :- prime(L), !.
range_prime(L, L, [], N) :- N =:= 1, !.

range_prime(N, L, Divisors, R) :-
        N =< L,
    	prime(N),
    	0 =:= mod(R, N),
    	R1 is div(R, N),
      	concat([N], LowDiv, Divisors),
        range_prime(N, L, LowDiv, R1).


range_prime(N, L, Divisors, R) :-
    N =< L,
    N1 is N + 1,
    range_prime(N1, L, Divisors, R).


