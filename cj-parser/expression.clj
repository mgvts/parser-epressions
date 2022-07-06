;==========================//==========================//==========================//==========================
;==========================//========================/hw10/========================//==========================
;==========================//==========================//==========================//==========================
(defn constant [value] (fn [_] value))
(defn variable [value] (fn [args] (get args value)))

(defn abstr_func [operation] (fn [a1 a2] (fn [args] (operation (a1 args) (a2 args)))))

(defn negate [a1] (fn [args] (- (a1 args))))
(def add (abstr_func +))
(def subtract (abstr_func -))
(def multiply (abstr_func *))
(def pow (abstr_func #(Math/pow %1 %2)))
(def log (abstr_func #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))))
(defn divide [a1 a2] (fn [args]
                       (/ (double (a1 args)) (double (a2 args)))))


(def operationsFuncs {'+      add,
                      '-      subtract,
                      '*      multiply,
                      '/      divide,,
                      'negate negate
                      'pow    pow
                      'log    log})

(defn parse [exp]
  (cond
    (number? exp) (constant exp)
    (symbol? exp) (variable (str exp))
    :else (apply (get operationsFuncs (first exp)) (mapv parse (rest exp)))))



(defn parseFunction [exp] (parse (read-string exp)))

;==========================//==========================//==========================//==========================
;==========================//========================/hw11/========================//==========================
;==========================//==========================//==========================//==========================

(definterface Operation
  (evaluate [])
  (toStr [])
  (toStringSuff [])
  (diff_rule [])
  )

(defn evaluate [e vars] ((.evaluate e) vars))
(defn toString [e] (.toStr e))
(defn diff [e var] ((.diff_rule e) var))
;
(defn toStringSuffix [e] (.toStringSuff e))


(declare ZERO)
(declare ONE)

(deftype Abstr_Constant [value]
  Operation
  (evaluate [_] (fn [_] value))
  (toStr [_] (str value))
  (toStringSuff [_] (str value))
  (diff_rule [_] (fn [_] ZERO))
  )

(defn Constant [val] (Abstr_Constant. val))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def E (Constant Math/E))


(deftype Abstr_Variable [name]
  Operation
  (evaluate [_] (fn [args] (args (clojure.string/lower-case (first name)))))
  (toStr [_] name)
  (toStringSuff [_] name)
  (diff_rule [_] (fn [var] (if (= var name)
                             ONE ZERO)))
  )

(defn Variable [name] (Abstr_Variable. name))

(deftype AbstrBin [symbol operation diffr a b]
  Operation
  (evaluate [_] (fn [kwargs] (operation (evaluate a kwargs) (evaluate b kwargs))))
  (toStr [_] (str "(" symbol " " (toString a) " " (toString b) ")"))
  (toStringSuff [_] (str "(" (toStringSuffix a) " " (toStringSuffix b) " " symbol ")"))
  (diff_rule [_] (fn [var] (diffr a b (diff a var) (diff b var))))
  )

(deftype AbstrUn [symbol operation diffr a]
  Operation
  (evaluate [_] (fn [kwargs] (operation (evaluate a kwargs))))
  (toStr [_] (str "(" symbol " " (toString a) ")"))
  (toStringSuff [_] (str "(" (toStringSuffix a) " " symbol ")"))
  (diff_rule [_] (fn [var] (diffr a (diff a var))))
  )

(defn Add [a1 a2] (AbstrBin. "+" +
                             (fn [a b da db] (Add da db)) a1 a2))

(defn Subtract [a1 a2] (AbstrBin. "-" -
                                  (fn [a b da db] (Subtract da db)) a1 a2))

(defn Multiply [a1 a2] (AbstrBin. "*" *
                                  (fn [a b da db] (Add (Multiply a db) (Multiply da b))) a1 a2))

(defn Divide [a1 a2] (AbstrBin. "/" #(/ (double %1) (double %2))
                                (fn [a b da db] (Divide (Subtract (Multiply da b) (Multiply a db)) (Multiply b b))) a1 a2))

(defn Negate [a1] (AbstrUn. "negate" -
                            (fn [a da] (Negate da)) a1))

;log_a(b) = (lnb)/(lna)
(defn Log [a1 a2] (AbstrBin. "log" (fn [f s] (/ (Math/log (Math/abs s)) (Math/log (Math/abs f))))
                             (fn [a b da db] (Divide (Subtract (Multiply (Log E a) (Divide db b)) (Multiply (Divide da a) (Log E b)))
                                                     (Multiply (Log E a) (Log E a)))) a1 a2))

;a^b = e^(b*lna)
(defn Pow [a1 a2] (AbstrBin. "pow" (fn [f s] (Math/pow f s))
                             (fn [a b da db] (Multiply (Pow a b)
                                                       (Add (Multiply b (Divide da a))
                                                            (Multiply db (Log E a))))) a1 a2))
(defn BitAnd [a1 a2] (AbstrBin. "&" #(Double/longBitsToDouble (bit-and (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
                                (fn [a b da db] (BitAnd a b)) a1 a2))

(defn BitOr [a1 a2] (AbstrBin. "|" #(Double/longBitsToDouble (bit-or (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
                               (fn [a b da db] (BitOr a b)) a1 a2))

(defn BitXor [a1 a2] (AbstrBin. "^" #(Double/longBitsToDouble (bit-xor (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
                                (fn [a b da db] (BitXor a b)) a1 a2))

(def operationsObj {'+           Add,
                    '-           Subtract,
                    '*           Multiply,
                    '/           Divide,
                    'negate      Negate,
                    'log         Log,
                    'pow         Pow,
                    '&           BitAnd,
                    '|           BitOr,
                    (symbol "^") BitXor
                    })

(defn parseObj [expr]
  (cond
    (number? expr) (Constant expr)
    (symbol? expr) (Variable (str expr))
    :else (apply (get operationsObj (first expr)) (mapv parseObj (rest expr)))))

(defn parseObject [expr] (parseObj (read-string expr)))

;==========================//==========================//==========================//==========================
;==========================//========================/hw12/========================//==========================
;==========================//==========================//==========================//==========================

;
;(load-file "parser.clj")
;(load-file "D:\\tryGoodProg\\paradigms-2022\\solution\\clojure-solutions\\parser.clj")
; This file should be placed in clojure-solutions
; You may use it via (load-file "parser.clj")

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)


(defn _empty [value] (partial -return value))

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))

(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
         ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0001})) (str input \u0001)))))
(mapv (_parser (_combine str (_char #{\a \b}) (_char #{\x}))) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])



(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))

(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))

(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce (partial _either) p ps))

(defn +opt [p]
  (+or p (_empty nil)))

(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))

(defn +plus [p] (+seqf cons p (+star p)))

(defn +str [p] (+map (partial apply str) p))

(def +parser _parser)


(defn +rules [defs]
  (cond
    (empty? defs) ()
    (seq? (first defs)) (let [[[name args body] & tail] defs]
                          (cons
                            {:name name :args args :body body}
                            (+rules tail)))
    :else (let [[name body & tail] defs]
            (cons
              {:name name :args [] :body body :plain true}
              (+rules tail)))))

(defmacro defparser [name & defs]
  (let [rules (+rules defs)
        plain (set (map :name (filter :plain rules)))]
    (letfn [(rule [{name :name, args :args, body :body}] `(~name ~args ~(convert body)))
            (convert [value]
              (cond
                (seq? value) (map convert value)
                (char? value) `(+char ~(str value))
                (contains? plain value) `(~value)
                :else value))]
      `(def ~name (letfn ~(mapv rule rules) (+parser (~(:name (last rules)))))))))



;;;

; :NOTE: не нужно парсить negate посимвольно
(def parseObjectSuffix
  (let [*space (+char " \t\n\r")
        *digit (+char "0123456789")
        *ws (+ignore (+star *space))
        *constant (+map #(Constant (read-string %)) (+str (+seqf cons (+opt (+char "-")) (+plus (+or *digit (+char "."))))))
        *variable (+map #(Variable %) (+str (+plus (+char "xyzXYZ"))))
        *operation (+map #(get operationsObj ((comp symbol str) %)) (+or (+char "+-*/&|^")
                                                                         (+str (+seq (+char "n") (+char "e") (+char "g") (+char "a") (+char "t") (+char "e")))))
        ]
    (letfn
      [(*args []
         (+seqn 1 (+char "(") *ws
                (+plus (+seqn 0 (+or (delay (*value)) *operation) *ws)) (+char ")")))
       (*bracket []
         (+map #(apply (last %) (butlast %)) (*args)))
       (*value [] (+or *variable *constant (*bracket)))]
      (+parser (+seqn 0 *ws (*value) *ws)))))





;(println (toStringSuffix (parseObjectSuffix "(x 2 +)")))
;
;(println (toStringSuffix (parseObjectSuffix "x")))


