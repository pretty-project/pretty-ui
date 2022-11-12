
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.string
    (:require [candy.api      :refer [param return]]
              [clojure.string :as string]
              [math.api       :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def empty-string "")
(def break        "\r\n")
(def tab          " ")
(def white-space  " ")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank?
  ; @param (*) n
  ;
  ; @usage
  ;  (blank? "")
  ;
  ; @return (boolean)
  [n]
  (string/blank? n))

(defn nonempty?
  ; @param (*) n
  ;
  ; @usage
  ;  (nonempty? "")
  ;
  ; @return (boolean)
  [n]
  (and (-> n string?)
       (-> n empty? not)))

(defn use-nil
  ; @param (*) n
  ;
  ; @example
  ;  (use-nil "")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (use-nil "abc")
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
  (let [n (str n)]
       (if (and (-> n empty? not)
                (> (count  n) dex))
           (nth n dex))))

(defn part
  ; @param (string) n
  ; @param (integer) start
  ; @param (integer)(opt) end
  ;
  ; @example
  ;  (part "abcdef" 2 4)
  ;  =>
  ;  "cd"
  ;
  ; @example
  ;  (part "abcdef" 4 2)
  ;  =>
  ;  "cd"
  ;
  ; @return (string)
  ([n start]
   (part n start (count n)))

  ([n start end]
   (let [n (str n)]
        (if (and (-> n empty? not)
                 (math/between? end   0 (count n))
                 (math/between? start 0 (count n)))
            (subs   n start end)
            (return n)))))

(defn trim
  ; @param (string) n
  ;
  ; @example
  ;  (trim " Foo  ")
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
  ;  (length "One Flew Over the Cuckoo's Nest")
  ;
  ; @return (integer)
  [n]
  (let [n (str n)]
       (if (empty? n)
           (return 0)
           (count  n))))

(defn join
  ; @param (collection) coll
  ; @param (nil or string) separator
  ; @param (map)(opt) options
  ;  {:join-empty? (boolean)(opt)
  ;    Default: true}
  ;
  ; @example
  ;  (join ["filename" "extension"] ".")
  ;  =>
  ;  "filename.extension"
  ;
  ; @example
  ;  (join ["a" "b" ""] ".")
  ;  =>
  ;  "a.b."
  ;
  ; @example
  ;  (join ["a" "b" ""] "." {:join-empty? false})
  ;  =>
  ;  "a.b"
  ;
  ; @return (string)
  ([coll separator]
   (join coll separator {}))

  ([coll separator {:keys [join-empty?]}]
   (let [last-dex (-> coll count dec)]
        (letfn [(separate?      [dex]  (and (not= dex last-dex)
                                            (-> (nth coll (inc dex)) str empty? not)))
                (join?          [part] (or (-> join-empty? false? not)
                                           (-> part str empty? not)))
                (f [result dex part] (if (join? part)
                                         (if (separate? dex)
                                             (str result part separator)
                                             (str result part))
                                         (return result)))]
               ; A reduce-kv csak vektor vagy térkép típust fogad, listát nem!
               (reduce-kv f "" (vec coll))))))

(defn max-length
  ; @param (*) n
  ; @param (integer) limit
  ;
  ; @example
  ;  (max-length "One Flew Over the Cuckoo's Nest" 10)
  ;  =>
  ;  "One Flew O"
  ;
  ; @example
  ;  (max-length "One Flew Over the Cuckoo's Nest" 10 " ...")
  ;  =>
  ;  "One Flew O ..."
  ;
  ; @return (string)
  [n limit & [suffix]]
  (let [n (str n)]
       (if (and (-> n empty? not)
                (integer?     limit)
                (> (length n) limit))
           (str (subs  n 0 limit) suffix)
           (return n))))

(defn min-length?
  ; @param (string) n
  ; @param (integer) min
  ;
  ; @example
  ;  (min-length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (min-length? "abc" 4)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n min]
  (let [n (str n)]
       (and     (integer?  min)
            (>= (length n) min))))

(defn max-length?
  ; @param (string) n
  ; @param (integer) max
  ;
  ; @example
  ;  (max-length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (max-length? "abc" 2)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n max]
  (let [n (str n)]
       (and     (integer?  max)
            (<= (length n) max))))

(defn length?
  ; @param (string) n
  ; @param (integer) x / min
  ; @param (integer)(opt) max
  ;
  ; @example
  ;  (length? "abc" 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (length? "abc" 2)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (length? "abc" 2 4)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (let [n (str n)]
        (= x (count n))))

  ([n min max]
   (let [n (str n)]
        (and (<= min (count n))
             (>= max (count n))))))

(defn split
  ; @param (string) n
  ; @param (regex) delimiter
  ;
  ; @example
  ;  (split "a.b.c" #".")
  ;  =>
  ;  ["a" "b" "c"]
  ;
  ; @example
  ;  (split ".b.c" #".")
  ;  =>
  ;  ["" "b" "c"]
  ;
  ; @example
  ;  (split ".b.c" #"_")
  ;  =>
  ;  [".b.c"]
  ;
  ; @return (strings in vector)
  [n delimiter]
  (cond (-> n str empty?) []
        (some? delimiter) (string/split n delimiter)
        :return           [(str n)]))

(defn prefix
  ; @param (string) n
  ; @param (*) prefix
  ;
  ; @example
  ;  (prefix "color" "@")
  ;  =>
  ;  "@color"
  ;
  ; @example
  ;  (prefix "" "@")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n prefix]
  (let [n      (str n)
        prefix (str prefix)]
       (if (or (-> n      empty?)
               (-> prefix empty?))
           (return     n)
           (str prefix n))))

(defn suffix
  ; @param (string) n
  ; @param (*) suffix
  ;
  ; @example
  ;  (suffix "420" "px")
  ;  =>
  ;  "420px"
  ;
  ; @example
  ;  (suffix "" "px")
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n suffix]
  (let [n      (str n)
        suffix (str suffix)]
       (if (or (-> n      empty?)
               (-> suffix empty?))
           (return n)
           (str    n suffix))))

(defn contains-part?
  ; @param (string) n
  ; @param (string) x
  ;
  ; @usage
  ;  (contains-part? "abc" "ab")
  ;
  ; @return (boolean)
  [n x]
  (let [n (str n)
        x (str x)]
       (if-not (or (-> n empty?)
                   (-> x empty?))
               (string/includes? n x))))

(defn insert-part
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (insert-part "ABCD" "xx" 2)
  ;  =>
  ;  "ABxxCD"
  ;
  ; @return (string)
  [n x dex]
  (let [n (str n)
        x (str x)]
       (if (empty? x)
           (return n)
           (let [count (count n)
                 dex   (math/between! dex 0 count)]
                (str (subs n 0 dex) x (subs n dex))))))

(defn first-dex-of
  ; @param (string) n
  ; @param (string) x
  ;
  ; @return (integer)
  [n x]
  (let [n (str n)
        x (str x)]
       (if-not (or (empty? n)
                   (empty? x))
               (string/index-of n x))))

(defn last-dex-of
  ; @param (string) n
  ; @param (string) x
  ;
  ; @return (integer)
  [n x]
  (let [n (str n)
        x (str x)]
       (if-not (or (empty? n)
                   (empty? x))
               (string/last-index-of n x))))

(defn nth-dex-of
  ; @param (string) n
  ; @param (string) x
  ; @param (integer) dex
  ;
  ; @example
  ;  (nth-dex-of "AbcAbcAbc" "A" 2)
  ;  =>
  ;  3
  ;
  ; @return (integer)
  [n x dex]
  (when (and (nonempty? n)
             (nonempty? x)
             (>=        dex 1))
        (letfn [(f [cursor lap]
                   (if-let [first-dex (-> n (subs cursor)
                                            (string/index-of x))]
                           (if (= lap dex)
                               (+ cursor first-dex)
                               (f (+ first-dex cursor 1)
                                  (inc lap)))))]
               (f 0 1))))

(defn before-first-occurence
  ; @param (string) n
  ; @param (string) x
  ; @param (map) options
  ;  {:return? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (before-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                          ", y")
  ;  =>
  ;  "With insomnia"
  ;
  ; @example
  ;  (before-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                          "abc")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (before-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                          "abc" {:return? true})
  ;  =>
  ;  "With insomnia, you're never really awake; but you're never really asleep."
  ;
  ; @return (string)
  ([n x]
   (before-first-occurence n x {:return? false}))

  ([n x {:keys [return?]}]
   (if (and (nonempty? n)
            (nonempty? x)
            (string/includes? n x))
       (subs n 0 (dec (+ (string/index-of n x)
                         (count x))))
       (if return? n))))

(defn before-last-occurence
  ; @param (string) n
  ; @param (string) x
  ; @param (map) options
  ;  {:return? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (before-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         ", y")
  ;  =>
  ;  "With insomnia"
  ;
  ; @example
  ;  (before-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         "abc")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (before-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         "abc" {:return? true})
  ;  =>
  ;  "With insomnia, you're never really awake; but you're never really asleep."
  ;
  ; @return (string)
  ([n x]
   (before-last-occurence n x {:return? false}))

  ([n x {:keys [return?]}]
   (if (and (nonempty? n)
            (nonempty? x)
            (string/includes? n x))
       (subs n 0 (string/last-index-of n x))
       (if return? n))))

(defn after-first-occurence
  ; @param (string) n
  ; @param (string) x
  ; @param (map) options
  ;  {:return? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (after-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         "never")
  ;  =>
  ;  "really asleep."
  ;
  ; @example
  ;  (after-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         "abc")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (after-first-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                         "abc" {:return? true})
  ;  =>
  ;  "With insomnia, you're never really awake; but you're never really asleep."
  ;
  ; @return (string)
  ([n x]
   (after-first-occurence n x {:return? false}))

  ([n x {:keys [return?]}]
   (if (and (nonempty? n)
            (nonempty? x)
            (string/includes? n x))
       (subs n (+ (string/index-of n x)
                  (count x)))
       (if return? n))))

(defn after-last-occurence
  ; @param (string) n
  ; @param (string) x
  ; @param (map) options
  ;  {:return? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (after-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                        "never")
  ;  =>
  ;  "really asleep."
  ;
  ; @example
  ;  (after-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                        "abc")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (after-last-occurence "With insomnia, you're never really awake; but you're never really asleep."
  ;                        "abc" {:return? true})
  ;  =>
  ;  "With insomnia, you're never really awake; but you're never really asleep."
  ;
  ; @return (string)
  ([n x]
   (after-last-occurence n x {:return? false}))

  ([n x {:keys [return?]}]
   (if (and (nonempty? n)
            (nonempty? x)
            (string/includes? n x))
       (subs n (+ (string/last-index-of n x)
                  (count x)))
       (if return? n))))

(defn remove-first-occurence
  ; @param (string) n
  ; @param (string) x
  ;
  ; @example
  ;  (remove-first-occurence "ABC-DEF-GHI" "-")
  ;  =>
  ;  "ABCDEF-GHI"
  ;
  ; @example
  ;  (remove-first-occurence "ABC-DEF-GHI" "%")
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
  ; @param (map) options
  ;  {:return? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (between-occurences "Hey You!" "H" "u")
  ;  =>
  ;  "ey Yo"
  ;
  ; @example
  ;  (between-occurences "aabbccdd" "a" "d")
  ;  =>
  ;  "abbccd"
  ;
  ; @example
  ;  (between-occurences "aabbccdd" "x" "y")
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
  ; @param (map)(opt)
  ;  {:separate-matches? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (count-occurences "abca" "a")
  ;  =>
  ;  2
  ;
  ; @example
  ;  (count-occurences "abca" "ab")
  ;  =>
  ;  1
  ;
  ; @example
  ;  (count-occurences "aaaa" "aa")
  ;  =>
  ;  3
  ;
  ; @example
  ;  (count-occurences "aaaa" "aa" {:separate-matches? true})
  ;  =>
  ;  2
  ;
  ; @return (integer)
  ([n x]
   (count-occurences n x {}))

  ([n x {:keys [separate-matches?]}]
   (if (contains-part? n x)
       (let [step (if separate-matches? (count x) 1)]
            (letfn [(f [cursor match-count]
                       (if-let [first-dex (first-dex-of (part n cursor) x)]
                               (let [step (if separate-matches? (count x) 1)]
                                    (f (+   cursor first-dex step)
                                       (inc match-count)))
                               (return match-count)))]
                   (f 0 0)))
       (return 0))))

(defn min-occurence?
  ; @param (string) n
  ; @param (string) x
  ; @param (integer) min
  ;
  ; @example
  ;  (min-occurence? "abca" "a" 2)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (min-occurence? "abca" "b" 2)
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
  ;  (ends-with? "The things you used to own, now they own you." ".")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (ends-with? "The things you used to own, now they own you." "")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (ends-with? n x {:case-sensitive? true}))

  ([n x {:keys [case-sensitive?]}]
                ; (ends-with? "Something" "")
   (boolean (or (= x "")
                ; (ends-with? "Something" nil)
                (= x nil)
                ; (ends-with? "" "")
                (= x n)
                ; (ends-with? ...)
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
  ;  (ends-with! "abc" "bc")
  ;  =>
  ;  "abc"
  ;
  ; @example
  ;  (ends-with! "abc" "x")
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
  ;  (not-ends-with! "abc" "bc")
  ;  =>
  ;  "a"
  ;
  ; @example
  ;  (not-ends-with! "abc" "x")
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
  ;  (starts-with? "On a long enough time line, the survival rate for everyone drops to zero."
  ;                "On a")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (starts-with? "On a long enough time line, the survival rate for everyone drops to zero."
  ;                "")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n x]
   (starts-with? n x {:case-sensitive? true}))

  ([n x {:keys [case-sensitive?]}]
                ; (starts-with? "Something" "")
   (boolean (or (= x "")
                ; (starts-with? "Something" nil)
                (= x nil)
                ; (starts-with? "" "")
                (= x n)
                ; (starts-with? ...)
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
  ;  (starts-with! "abc" "ab")
  ;  =>
  ;  "abc"
  ;
  ; @example
  ;  (starts-with! "abc" "x")
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
  ;  (not-starts-with! "abc" "ab")
  ;  =>
  ;  "c"
  ;
  ; @example
  ;  (not-starts-with! "abc" "x")
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
  ;  (pass-with? "abc" "ab")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (pass-with? "abc" "abc")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (pass-with? "abc" "Abc")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (pass-with? "abc" "Abc" {:case-sensitive? false})
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
  ;  (not-pass-with? "abc" "ab")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (not-pass-with? "abc" "abc")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (not-pass-with? "abc" "Abc")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (not-pass-with? "abc" "Abc" {:case-sensitive? false})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  ([n x]
   (not-pass-with? n x {:case-sensitive? true}))

  ([n x options]
   (let [pass-with? (pass-with? n x options)]
        (not pass-with?))))

(defn replace-part
  ; @param (string) n
  ; @param (function or string) x
  ; @param (string) y
  ;
  ; @example
  ;  (replace-part "ABC" #"B" "X")
  ;  =>
  ;  "AXC"
  ;
  ; @example
  ;  (replace-part "ABC" #"B" nil)
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n x y]
  (string/replace (str n) x (str y)))

(defn use-replacements
  ; @param (string) n
  ; @param (numbers or strings in vector) replacements
  ;
  ; @example
  ;  (use-replacements "Hi, my name is %" ["John"])
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @example
  ;  (use-replacements "My favorite colors are: %1, %2 and %3" ["red" "green" "blue"])
  ;  =>
  ;  "My favorite colors are: red, green and blue"
  ;
  ; @example
  ;  (use-replacements "%1 / %2 items downloaded" [nil 3])
  ;  =>
  ;  ""
  ;
  ; @return (string)
  [n replacements]
  ; XXX#4509
  ;
  ; A behelyettesíthetőséget jelző karakter abban az esetben van számmal jelölve,
  ; (pl. %1, %2, ...) ha a szöveg több behelyettesítést fogad.
  ;
  ; Hasonlóan az anoním függvények paramétereinek elnevezéséhez, ahol az EGY
  ; paramétert fogadó függvények egyetlen paraméterének neve egy számmal NEM
  ; megkülönböztett % karakter és a TÖBB paramétert fogadó függvények paramétereinek
  ; nevei pedig számokkal megkülönböztetett %1, %2, ... elnevezések!
  ;
  ; Abban az esetben, ha valamelyik behelyettesítő kifejezés értéke üres (nil, "")
  ; a függvény visszatérési értéke egy üres string ("")!
  ; Emiatt nem szükséges máshol kezelni, hogy ne jelenjenek meg a hiányos feliratok,
  ; mert ez a use-replacements függvényben kezelve van!
  (when (and (nonempty? n)
             (vector?   replacements))
        (letfn [; ...
                (f? [] (= 1 (count replacements)))
                ; ...
                (f1 [n marker replacement]
                    ; A replacement értéke number és string típus is lehet!
                    (if-not (-> replacement str empty?)
                            (string/replace n marker replacement)))
                ; ...
                (f2 [n dex replacement]
                    (let [marker (str "%" (inc dex))]
                         (f1 n marker replacement)))]
               ; ...
               (if (f?) (f1 n "%" (first replacements))
                        (reduce-kv f2 n replacements)))))

(defn use-replacement
  ; @param (string) n
  ; @param (string) replacement
  ;
  ; @example
  ;  (use-replacement "Hi, my name is %" "John")
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @return (string)
  [n replacement]
  (replace-part n "%" replacement))

(defn use-placeholder
  ; @param (string) n
  ; @param (string) placeholder
  ;
  ; @example
  ;  (use-placeholder "My content" "My placeholder")
  ;  =>
  ;  "My content"
  ;
  ; @example
  ;  (use-placeholder "" "My placeholder")
  ;  =>
  ;  "My placeholder"
  ;
  ; @return (string)
  [n placeholder]
  (if (nonempty? n)
      (return    n)
      (return    placeholder)))

(defn filter-characters
  ; @param (string) n
  ; @param (vector) allowed-characters
  ;
  ; @example
  ;  (filter-characters "+3630 / 123 - 4567" ["+" "1" "2" "3" "4" "5" "6" "7" "8" "9" "0"])
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
  ;  (remove-part "ABC" "B")
  ;
  ; @return (string)
  [n x]
  (replace-part n x ""))

(defn remove-newlines
  ; @param (string) n
  ;
  ; @usage
  ;  (remove-newlines "ABC\r\n")
  ;
  ; @return (string)
  [n]
  (remove-part n \newline))

(defn count-newlines
  ; @param (string) n
  ;
  ; @usage
  ;  (count-newlines "ABC")
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
  ;    Default: false}
  ;
  ; @example
  ;  (max-lines "abc\ndef\nghi" 2)
  ;  =>
  ;  "abc\ndef"
  ;
  ; @example
  ;  (max-lines "abc\ndef\nghi" 2 {:reverse? true})
  ;  =>
  ;  "def\nghi"
  ;
  ; @return (string)
  ([n limit]
   (max-lines n limit {:reverse? false}))

  ([n limit {:keys [reverse?]}]
   (if (nonempty? n)
       (let [lines (split n #"\n")
             count (count lines)
             limit (min   limit count)
             lines (if reverse? (subvec lines (- count limit) count)
                                (subvec lines 0 limit))]
            (letfn [(f [result dex]
                       (if (= dex limit)
                           (return result)
                           (f (str result (if (not= dex 0) "\n") (nth lines dex))
                              (inc dex))))]
                   (f "" 0))))))

(defn paren
  ; @param (string) n
  ;
  ; @example
  ;  (paren "420")
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
  ;  (bracket "420")
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
  ;  (percent "99.999")
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
  ;  (quotes "420")
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
  ;  (capitalize "to-capitalize")
  ;
  ; @return (string)
  [n]
  (-> n str string/capitalize))


(defn uppercase
  ; @param (string) n
  ;
  ; @example
  ;  (uppercase "uppercase")
  ;  =>
  ;  "UPPERCASE"
  ;
  ; @return (string)
  [n]
  (-> n str string/upper-case))

(defn lowercase
  ; @param (string) n
  ;
  ; @example
  ;  (lowercase "LOWERCASE")
  ;  =>
  ;  "lowercase"
  ;
  ; @return (string)
  [n]
  (-> n str string/lower-case))

(defn contains-lowercase-letter?
  ; @param (string) n
  ;
  ; @usage
  ;  (contains-lowercase-letter? "abCd12")
  ;
  ; @return (boolean)
  [n]
  (not= n (uppercase n)))

(defn contains-uppercase-letter?
  ; @param (string) n
  ;
  ; @usage
  ;  (contains-uppercase-letter? "abCd12")
  ;
  ; @return (boolean)
  [n]
  (not= n (lowercase n)))

(defn snake-case
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @example
  ;  (snake-case "SnakeCase")
  ;  =>
  ;  "snake-case"
  ;
  ; @return (string)
  [n]
  (let [count (count n)]
       (letfn [(f [result cursor]
                  (if (= count cursor)
                      (return result)
                      (let [char (subs n cursor (inc cursor))]
                           (if (= char (uppercase char))
                               (f (str (subs n 0 cursor)
                                       (if (not= cursor 0) "-")
                                       (lowercase char)
                                       (subs n (inc cursor)))
                                  (inc cursor))
                               (f result (inc cursor))))))]
              (f n 0))))

(defn CamelCase
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @example
  ;  (CamelCase "camel-case")
  ;  =>
  ;  "CamelCase"
  ;
  ; @return (string)
  [n])
  ; TODO

(defn abc?
  ; @param (list of strings)
  ;
  ; @example
  ;  (abc? "abc" "def")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (abc? "1a" "0a")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (abc? "xyz" "xyz")
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
  ;  (to-integer "10")
  ;  =>
  ;  10
  ;
  ; @return (integer)
  [n]
  #?(:cljs (js/parseInt n)
     :clj  (Integer. (re-find #"\d+" n))))
