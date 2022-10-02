
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.string
    (:require [clojure.string   :as string]
              [mid-fruits.candy :refer [param return]]
              [mid-fruits.math  :as math]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def empty-string "")
(def break        "\r\n")
(def tab          " ")
(def white-space  " ")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (*) n
  ;
  ; @usage
  ;  (string/nonempty? "")
  ;
  ; @return (boolean)
  [n]
  (and      (string? n)
       (not (empty?  n))))

(defn use-nil
  ; @param (*) n
  ;
  ; @example
  ;  (string/use-nil "")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (string/use-nil "abc")
  ;  =>
  ;  "abc"
  ;
  ; @return (boolean)
  [n]
  (if (empty? n)
      (return nil)
      (return n)))

(defn get-nth-character
  ; @param (string) n
  ; @param (integer) dex
  ;
  ; @param (string)
  [n dex]
  (when (and (nonempty? n)
             (>  (count n) dex))
        (nth n dex)))

(defn part
  ; @param (string) n
  ; @param (integer) start
  ; @param (integer) end
  ;
  ; @example
  ;  (string/part "abcdef" 2 4)
  ;  =>
  ;  "cd"
  ;
  ; @example
  ;  (string/part "abcdef" 4 2)
  ;  =>
  ;  "cd"
  ;
  ; @return (string)
  [n start end]
  (if (and (nonempty? n)
           (math/between? end   0 (count n))
           (math/between? start 0 (count n)))
      (subs   n start end)
      (return n)))

(defn trim
  ; @param (string) n
  ;
  ; @example
  ;  (string/trim " Foo  ")
  ;  =>
  ;  "Foo"
  ;
  ; @return (string)
  [n]
  (-> n str string/trim))

(defn length
  ; @param (string) n
  ;
  ; @usage
  ;  (string/length "One Flew Over the Cuckoo's Nest")
  ;
  ; @return (integer)
  [n]
  (if (nonempty? n)
      (count     n)
      (return    0)))

(defn join
  ; @param (collection) coll
  ; @param (nil or string) separator
  ; @param (map)(opt) options
  ;  {:join-empty? (boolean)(opt)
  ;    Default: true}
  ;
  ; @example
  ;  (string/join ["filename" "extension"] ".")
  ;  =>
  ;  "filename.extension"
  ;
  ; @example
  ;  (string/join ["a" "b" ""] ".")
  ;  =>
  ;  "a.b."
  ;
  ; @example
  ;  (string/join ["a" "b" ""] "." {:join-empty? false})
  ;  =>
  ;  "a.b"
  ;
  ; @return (string)
  ([coll separator]
   (join coll separator {}))

  ([coll separator {:keys [join-empty?]}]
   (let [last-dex (-> coll count dec)]
        (letfn [(separate?      [dex]  (and (not= dex last-dex)
                                            (-> (nth coll (inc dex))
                                                str empty? not)))
                (join?          [part] (or (-> join-empty? false? not)
                                           (-> part str empty? not)))
                (f [result dex part] (if (join? part)
                                         (if (separate? dex)
                                             (str result part separator)
                                             (str result part))
                                         (return result)))]
               (reduce-kv f "" coll)))))

(defn max-length
  ; @param (*) n
  ; @param (integer) limit
  ;
  ; @example
  ;  (string/max-length "One Flew Over the Cuckoo's Nest" 10)
  ;  =>
  ;  "One Flew O"
  ;
  ; @example
  ;  (string/max-length "One Flew Over the Cuckoo's Nest" 10 " ...")
  ;  =>
  ;  "One Flew O ..."
  ;
  ; @return (string)
  [n limit & [suffix]]
  (let [n (str n)]
       (if (and (nonempty? n)
                (integer?  limit)
                (> (length n) limit))
           (str (subs  n 0 limit) suffix)
           (return n))))

(defn min-length?
  ; @param (string) n
  ; @param (integer) min
  ;
  ; @example
  ;  (string/min-length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/min-length? "abc" 4)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n min]
  (and     (string?  n)
           (integer? min)
       (>= (length   n) min)))

(defn max-length?
  ; @param (string) n
  ; @param (integer) max
  ;
  ; @example
  ;  (string/max-length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/max-length? "abc" 2)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n max]
  (and     (string?  n)
           (integer? max)
       (<= (length   n) max)))

(defn length?
  ; @param (string) n
  ; @param (integer) x / min
  ; @param (integer)(opt) max
  ;
  ; @example
  ;  (string/length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/length? "abc" 2)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (string/length? "abc" 2 4)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (and      (string? n)
        (= x (count   n))))

  ([n min max]
   (and         (string? n)
        (<= min (count   n))
        (>= max (count   n)))))

(defn split
  ; @param (string) n
  ; @param (regex) delimiter
  ;
  ; @example
  ;  (string/split "a.b.c" #".")
  ;  =>
  ;  ["a" "b" "c"]
  ;
  ; @example
  ;  (string/split ".b.c" #".")
  ;  =>
  ;  ["" "b" "c"]
  ;
  ; @example
  ;  (string/split ".b.c" #"_")
  ;  =>
  ;  []
  ;
  ; @return (vector)
  [n delimiter]
  (if (and delimiter (nonempty? n))
      (string/split n delimiter)
      (return [])))

(defn suffix
  ; @param (string) n
  ; @param (*) suffix
  ;
  ; @usage
  ;  (string/suffix "420" "px")
  ;
  ; @return (string)
  [n suffix]
  (if (and (nonempty? n)
           (nonempty? suffix))
      (str    n tab suffix)
      (return n)))

(defn insert-part
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/insert-part "ABCD" "xx" 2)
  ;  =>
  ;  "ABxxCD"
  ;
  ; @return (string)
  [n x dex]
  (if (nonempty? x)
      (let [count (count n)
            dex   (math/between! dex 0 count)]
           (str (subs n 0 dex) x (subs n dex)))
      (return n)))

(defn before-first-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/before-first-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   ", y")
  ;  =>
  ;  "With insomnia"
  ;
  ; @example
  ;  (string/before-first-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "abc")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x)
             (string/includes? n x))
        (subs n 0 (dec (+ (string/index-of n x)
                          (count x))))))

(defn before-last-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/before-last-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   ", y")
  ;  =>
  ;  "With insomnia"
  ;
  ; @example
  ;  (string/before-first-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "abc")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x)
             (string/includes? n x))
        (subs n 0 (string/last-index-of n x))))

(defn first-index-of
  ; @param (string) n
  ; @param (string) x
  ;
  ; @return (integer)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x))
        (string/index-of n x)))

(defn last-index-of
  ; @param (string) n
  ; @param (string) x
  ;
  ; @return (integer)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x))
        (string/last-index-of n x)))

(defn after-first-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/after-first-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "never")
  ;  =>
  ;  "really asleep."
  ;
  ; @example
  ;  (string/after-first-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "abc")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x)
             (string/includes? n x))
        (subs n (+ (string/index-of n x)
                   (count x)))))

(defn after-last-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/after-last-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "never")
  ;  =>
  ;  "really asleep."
  ;
  ; @example
  ;  (string/after-last-occurence
  ;   "With insomnia, you're never really awake; but you're never really asleep."
  ;   "abc")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x)
             (string/includes? n x))
        (subs n (+ (string/last-index-of n x)
                   (count x)))))

(defn remove-first-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/remove-first-occurence "ABC-DEF-GHI" "-")
  ;  =>
  ;  "ABCDEF-GHI"
  ;
  ; @example
  ;  (string/remove-first-occurence "ABC-DEF-GHI" "%")
  ;  =>
  ;  "ABC-DEF-GHI"
  ;
  ; @return (string)
  [n x]
  (if (and (nonempty? n)
           (nonempty? x)
           (string/includes? n x))
      (let [dex   (string/index-of n x)
            count (count             x)]
           (str (subs n 0 (dec (+ dex count)))
                (subs n        (+ dex count))))
      (return n)))

(defn between-occurences
  ; @param (string) n
  ; @param (string) x
  ; @param (string) y
  ;
  ; @example
  ;  (string/between-occurences "Hey You!" "H" "u")
  ;  =>
  ;  "ey Yo"
  ;
  ; @example
  ;  (string/between-occurences "aabbccdd" "a" "d")
  ;  =>
  ;  "abbccd"
  ;
  ; @example
  ;  (string/between-occurences "aabbccdd" "x" "y")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [n x y]
  (-> n (after-first-occurence x)
        (before-last-occurence y)))

(defn count-occurences
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (string/count-occurences "abca" "a")
  ;  =>
  ;  2
  ;
  ; @return (integer)
  [n x]
  (if (nonempty? n)
      (get (frequencies n) x)
      (return 0)))

(defn min-occurence?
  ; @param (string) n
  ; @param (string) x
  ; @param (integer) min
  ;
  ; @example
  ;  (string/min-occurence? "abca" "a" 2)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/min-occurence? "abca" "b" 2)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n x min]
  (let [occurence-count (count-occurences n x)]
       (<= min occurence-count)))

(defn max-occurence?
  ; @param (string) n
  ; @param (string) x
  ; @param (integer) max
  ;
  ; @return (boolean)
  [n x max])

(defn ends-with?
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/ends-with? "The things you used to own, now they own you." ".")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/ends-with? "The things you used to own, now they own you." "")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (ends-with? n x {:case-sensitive? true}))

  ([n x {:keys [case-sensitive?]}]
                ; (string/ends-with? "Something" "")
   (boolean (or (= x "")
                ; (string/ends-with? "Something" nil)
                (= x nil)
                ; (string/ends-with? "" "")
                (= x n)
                ; (string/ends-with? ...)
                (when (and (nonempty? n)
                           (nonempty? x))
                      (if case-sensitive? (string/ends-with? n x)
                                          (string/ends-with? (string/lower-case n)
                                                             (string/lower-case x))))))))

(defn not-ends-with?
  [n x])

(defn ends-with!
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/ends-with! "abc" "bc")
  ;  =>
  ;  "abc"
  ;
  ; @example
  ;  (string/ends-with! "abc" "x")
  ;  =>
  ;  "abcx"
  ;
  ; @return (string)
  ([n x]
   (ends-with! n x {:case-sensitive? true}))

  ([n x options]
   (if (ends-with? n x options)
       (return n)
       (str    n x))))

(defn not-ends-with!
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/not-ends-with! "abc" "bc")
  ;  =>
  ;  "a"
  ;
  ; @example
  ;  (string/not-ends-with! "abc" "x")
  ;  =>
  ;  "abc"
  ;
  ; @return (string)
  ([n x]
   (not-ends-with! n x {:case-sensitive? true}))

  ([n x options]
   (if (ends-with? n x options)
       (before-last-occurence n x)
       (return n))))

(defn starts-with?
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/starts-with?
  ;   "On a long enough time line, the survival rate for everyone drops to zero."
  ;   "On a")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/starts-with?
  ;   "On a long enough time line, the survival rate for everyone drops to zero."
  ;   "")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (starts-with? n x {:case-sensitive? true}))

  ([n x {:keys [case-sensitive?]}]
                ; (string/starts-with? "Something" "")
   (boolean (or (= x "")
                ; (string/starts-with? "Something" nil)
                (= x nil)
                ; (string/starts-with? "" "")
                (= x n)
                ; (string/starts-with? ...)
                (when (and (nonempty? n)
                           (nonempty? x))
                      (if case-sensitive? (string/starts-with? n x)
                                          (string/starts-with? (string/lower-case n)
                                                               (string/lower-case x))))))))

(defn not-starts-with?
  [n x])

(defn starts-with!
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/starts-with! "abc" "ab")
  ;  =>
  ;  "abc"
  ;
  ; @example
  ;  (string/starts-with! "abc" "x")
  ;  =>
  ;  "xabc"
  ;
  ; @return (string)
  ([n x]
   (starts-with! n x {:case-sensitive? true}))

  ([n x options]
   (if (starts-with? n x options)
       (return n)
       (str    x n))))

(defn not-starts-with!
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/not-starts-with! "abc" "ab")
  ;  =>
  ;  "c"
  ;
  ; @example
  ;  (string/not-starts-with! "abc" "x")
  ;  =>
  ;  "abc"
  ;
  ; @return (string)
  ([n x]
   (not-starts-with! n x {:case-sensitive? true}))

  ([n x options]
   (if (starts-with? n x options)
       (after-first-occurence n x)
       (return n))))

(defn pass-with?
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/pass-with? "abc" "ab")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (string/pass-with? "abc" "abc")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/pass-with? "abc" "Abc")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (string/pass-with? "abc" "Abc" {:case-sensitive? false})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (pass-with? n x {:case-sensitive? true}))

  ([n x {:keys [case-sensitive?]}]
   (or (= n x)
       (and (not case-sensitive?)
            (nonempty? n)
            (nonempty? x)
            (= (string/lower-case n)
               (string/lower-case x))))))

(defn not-pass-with?
  ; @param (string) n
  ; @param (string) x
  ; @param (map)(opt) options
  ;  {:case-sensitive? (boolean)
  ;    Default: true}
  ;
  ; @example
  ;  (string/not-pass-with? "abc" "ab")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/not-pass-with? "abc" "abc")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (string/not-pass-with? "abc" "Abc")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/not-pass-with? "abc" "Abc" {:case-sensitive? false})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  ([n x]
   (not-pass-with? n x {:case-sensitive? true}))

  ([n x options]
   (let [pass-with? (pass-with? n x options)]
        (not pass-with?))))

(defn contains-part?
  ; @param (string) n
  ; @param (string) x
  ;
  ; @usage
  ;  (string/contains-part? "abc" "ab")
  ;
  ; @return (boolean)
  [n x]
  (when (and (nonempty? n)
             (nonempty? x))
        (string/includes? n x)))

(defn replace-part
  ; @param (string) n
  ; @param (function or string) x
  ; @param (function or string) y
  ;
  ; @example
  ;  (string/replace-part "ABC" #"B" "X")
  ;  =>
  ;  "AXC"
  ;
  ; @example
  ;  (string/replace-part "ABC" #"B" nil)
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n x y]
  (str (when (and x y (nonempty? n))
             (string/replace n x y))))

(defn use-replacements
  ; XXX#4509
  ;
  ; @param (string) n
  ; @param (vector) replacements
  ;
  ; @example
  ;  (string/use-replacements "Hi, my name is %" ["John"])
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @example
  ;  (string/use-replacements "My favorite colors are: %1, %2 and %3" ["red" "green" "blue"])
  ;  =>
  ;  "My favorite colors are: red, green and blue"
  ;
  ; @example
  ;  (string/use-replacements "%1 / %2 items downloaded" [nil nil])
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n replacements]
  (when (and (nonempty? n)
             (vector?   replacements))
        (if (= 1 (count replacements))
            ; Use one replacement ...
            (replace-part n "%" (first replacements))
            ; Use more replacements ...
            (reduce-kv #(string/replace %1 (str "%" (inc %2)) %3) n replacements))))

(defn use-replacement
  ; @param (string) n
  ; @param (string) replacement
  ;
  ; @example
  ;  (string/use-replacement "Hi, my name is %" "John")
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @return (string)
  [n replacement]
  (replace-part n "%" replacement))

(defn filter-characters
  ; @param (string) n
  ; @param (vector) allowed-characters
  ;
  ; @example
  ;  (string/filter-characters "+3630 / 123 - 4567" ["+" "1" "2" "3" "4" "5" "6" "7" "8" "9" "0"])
  ;  =>
  ;  "+36301234567"
  ;
  ; @return (string)
  [n allowed-characters]
  (letfn [(filter-characters-f [o x] (if (some #(= x %) allowed-characters)
                                         (str o x) o))]
         (reduce filter-characters-f "" n)))

(defn remove-part
  ; @param (string) n
  ; @param (string) x
  ;
  ; @usage
  ;  (string/remove-part "ABC" "B")
  ;
  ; @return (string)
  [n x]
  (replace-part n x ""))

(defn remove-newlines
  ; @param (string) n
  ;
  ; @usage
  ;  (string/remove-newlines "ABC\r\n")
  ;
  ; @return (string)
  [n]
  (remove-part n \newline))

(defn count-newlines
  ; @param (string) n
  ;
  ; @usage
  ;  (string/count-newlines "ABC")
  ;
  ; @return (integer)
  [n]
  (if (nonempty? n)
      (get (frequencies n) \newline)
      (return 0)))

(defn max-lines
  ; @param (string) n
  ; @param (integer) limit
  ; @param (map)(opt) options
  ;  {:reverse? (boolean)(opt)
  ;    Default: false
  ;    TODO: az utolsó x sort tartja meg }
  ;
  ; @return (string)
  ([n limit]
   n)

  ([n limit {:keys [reverse?]}]
   n))

(defn paren
  ; @param (string) n
  ;
  ; @example
  ;  (string/paren "420")
  ;  =>
  ;  "(420)"
  ;
  ; @return (string)
  [n]
  (str (when n (str "(" n ")"))))

(defn bracket
  ; @param (string) n
  ;
  ; @example
  ;  (string/bracket "420")
  ;  =>
  ;  "[420]"
  ;
  ; @return (string)
  [n]
  (str (when n (str "[" n "]"))))

(defn percent
  ; @param (string) n
  ;
  ; @example
  ;  (string/percent "99.999")
  ;  =>
  ;  "99.999%"
  ;
  ; @return (string)
  [n]
  (str (when n (str n "%"))))

(defn quotes
  ; @param (string) n
  ;
  ; @example
  ;  (string/quotes "420")
  ;  =>
  ;  "\"420\""
  ;
  ; @return (string)
  [n]
  (str (when n (str "\"" n "\""))))

(defn capitalize
  ; @param (string) n
  ;
  ; @usage
  ;  (string/capitalize "to-capitalize")
  ;
  ; @return (string)
  [n]
  (-> n str string/capitalize))


(defn uppercase
  ; @param (string) n
  ;
  ; @usage
  ;  (string/uppercase "to-uppercase")
  ;
  ; @return (string)
  [n]
  (-> n str string/upper-case))

(defn lowercase
  ; @param (string) n
  ;
  ; @usage
  ;  (string/lowercase "TO-LOWERCASE")
  ;
  ; @return (string)
  [n]
  (-> n str string/lower-case))

(defn contains-lowercase-letter?
  ; @param (string) n
  ;
  ; @usage
  ;  (string/contains-lowercase-letter? "abCd12")
  ;
  ; @return (boolean)
  [n]
  (not= n (uppercase n)))

(defn contains-uppercase-letter?
  ; @param (string) n
  ;
  ; @usage
  ;  (string/contains-uppercase-letter? "abCd12")
  ;
  ; @return (boolean)
  [n]
  (not= n (lowercase n)))

(defn snake-case
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (string/snake-case "toSnakeCase")
  ;
  ; @return (string)
  [n]
  (return n))

(defn CamelCase
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (string/CamelCase "to-camel-case")
  ;
  ; @return (string)
  [n]
  (return n))

(defn abc?
  ; @param (list of strings)
  ;
  ; @example
  ;  (string/abc? "abc" "def")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (string/abc? "1a" "0a")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (string/abc? "xyz" "xyz")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [x y]
  (>= 0 (compare x y)))

(defn to-integer
  ; @param (string) n
  ;
  ; @example
  ;  (string/to-integer "10")
  ;  =>
  ;  10
  ;
  ; @return (integer)
  [n]
  #?(:cljs (js/parseInt n)
     :clj  (Integer. (re-find #"\d+" n))))
