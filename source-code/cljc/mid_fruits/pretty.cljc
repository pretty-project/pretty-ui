
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.06
; Description:
; Version: v0.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.pretty
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.loop   :refer [reduce+first? reduce-kv+first?]]
              [mid-fruits.map    :as map]
              [mid-fruits.vector :as vector]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- string->wrap?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @example
  ;  (string->wrap? "[:a :b :c]")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (string/min-length? n 60))

(defn- mixed->wrap-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (mixed->wrap-items? [:a :b :c])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (boolean (and (or (map?    n)
                    (vector? n))
                (string->wrap? (str n)))))

(defn- remove-unnecessary-breaks
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @return (string)
  [n]
  (string/replace-part n #"\r\n}" "}"))

(defn- key-tabs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) depth
  ;
  ; @example
  ;  (key-tabs 2)
  ;  =>
  ;  "  "
  ;
  ; @return (string)
  [depth]
  (apply str (repeat depth string/tab)))

(defn- break
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (n) string
  ; @param (integer) depth
  ;
  ; @example
  ;  (break "{:foo "bar"}" 2)
  ;  =>
  ;  "\r\n  "
  ;
  ; @return (string)
  [n depth]
  (let [key-tabs (key-tabs depth)]
       (str string/break key-tabs n)))

(defn- map-wrap
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (n) string
  ;
  ; @example
  ;  (map-wrap ":foo []")
  ;  =>
  ;  "{:foo []}"
  ;
  ; @return (string)
  [n]
  (str "{" n "}"))

(defn- vector-wrap
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @example
  ;  (vector-wrap ":foo :bar")
  ;  =>
  ;  "[:foo :bar]"
  ;
  ; @return (string)
  [n]
  (str "[" n "]"))

(defn- append-vector-v
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) o
  ; @param (string) v
  ; @param (map) pretty-props
  ;  {:depth (integer)
  ;   :first-item? (boolean)
  ;   :wrap-items? (boolean)}
  ;
  ; @example
  ;  TODO ...
  ;
  ; @return (string)
  [o v {:keys [depth first-item? wrap-items?]}]
  (let [v (if (and wrap-items? (not first-item?)) (break v (inc depth)) v)]
       (cond first-item? (str o v)
             :else (str o string/tab v))))

(defn- append-map-kv
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) o
  ; @param (string) k
  ; @param (string) v
  ; @param (map) pretty-props
  ;  {:depth (integer)
  ;   :first-item? (boolean)
  ;   :wrap-items? (boolean)}
  ;
  ; @example
  ;  TODO ...
  ;
  ; @return (string)
  [o k v {:keys [depth first-item? wrap-items?]}]
  (let [k (if (and wrap-items? (not first-item?)) (break k (inc depth)) k)
        v (if (string->wrap? v) (break v (inc depth)) v)]
       (cond first-item? (str o k string/tab v)
             :else  (str o string/tab k string/tab v))))

(defn fn->string
  ; @param (function) n
  ;
  ; @return (string)
  [_]
  (str "$FUNCTION"))

(defn float->string
  ; @param (float) n
  ;
  ; @return (string)
  [n]
  (str n))

(defn integer->string
  ; @param (integer) n
  ;
  ; @return (string)
  [n]
  (str n))

(defn nil->string
  ; @param (nil) n
  ;
  ; @return (string)
  [_]
  (str "nil"))

(defn string->string
  ; @param (string) n
  ;
  ; A (pr-str *) függvény használata biztosítja, hogy az n paraméterben levő
  ; escape-elt quote karakterek (\") megmaradjanak.
  ; Az (str *) függvény kiértékeli a n string tartalmát és eltávolítja az
  ; escape-eléshez szükséges visszaper karaktereket.
  ;
  ; @return (string)
  [n]
 ;(str "\"" n "\"")
  (pr-str n))

(defn var->string
  ; @param (symbol) n
  ;
  ; @return (string)
  [_]
  (str "$SYMBOL"))

(defn mixed->string
  ; <pre> HTML-tag segít human-readable formátumként megjeleníteni a mixed->string
  ; függvény kimenetét
  ;
  ; @param (*) n
  ; @param (map) options
  ;  {:abc? (boolean)
  ;    Alphabetical ordered map items
  ;    Default: false}
  ;
  ; @usage
  ;  (pretty/mixed->string {:a {:b "a/b"}})
  ;
  ; @usage
  ;  [:pre (pretty/mixed->string {:a {:b "a/b"}})]
  ;
  ; @return (string)
  ([n]
   (mixed->string n {}))

  ([n {:keys [abc?]}]
   (letfn [(map->string
             ; @param (map) n
             ; @param (map) pretty-props
             ;  {:depth (integer)
             ;   :wrap-items? (boolean)(opt)}
             ;
             ; @return (string)
             [n {:keys [depth wrap-items?]}]
             (map-wrap (reduce-kv+first? (fn [o k v first?]
                                             (append-map-kv o (mixed->string k {:depth 0})
                                                              (mixed->string v {:depth       (inc depth)
                                                                                :wrap-items? (mixed->wrap-items? v)})
                                                              {:depth       depth
                                                               :first-item? first?
                                                               :wrap-items? wrap-items?}))

                                         (param nil)
                                         (param n))))

           (map->ordered-string
             ; @param (map) n
             ; @param (map) pretty-props
             ;  {:depth (integer)
             ;   :wrap-items? (boolean)(opt)}
             ;
             ; @return (string)
             [n {:keys [depth wrap-items?]}]
             (let [ordered-keys (vector/abc (keys n))]
                  (map-wrap (reduce+first? (fn [o k first?]
                                               (let [v (get n k)]
                                                    (append-map-kv o (mixed->string k {:depth 0})
                                                                     (mixed->string v {:depth       (inc depth)
                                                                                       :wrap-items? (mixed->wrap-items? v)})

                                                                     {:depth       depth
                                                                      :first-item? first?
                                                                      :wrap-items? wrap-items?})))
                                           (param nil)
                                           (param ordered-keys)))))

           (vector->string
             ; @param (vector) n
             ; @param (map) pretty-props
             ;  {:depth (integer)
             ;   :wrap-items? (boolean)(opt)}
             ;
             ; @return (string)
             [n {:keys [depth wrap-items?]}]
             (vector-wrap (reduce+first? (fn [o v first?]
                                             (append-vector-v o (mixed->string v {:depth       (inc depth)
                                                                                  :wrap-items? (mixed->wrap-items? v)})

                                                                {:depth       depth
                                                                 :first-item? first?
                                                                 :wrap-items? wrap-items?}))
                                         (param nil)
                                         (param n))))

           (mixed->string
             ; @param (*) n
             ; @param (map) pretty-props
             ;  {:depth (integer)
             ;   :wrap-items? (boolean)(opt)}
             ;
             ; @return (string)
             [n {:keys [depth wrap-items?] :as pretty-props}]
             (cond (and (map? n) abc?)    (map->ordered-string n pretty-props)
                   (map? n)               (map->string         n pretty-props)
                   (vector? n)            (vector->string      n pretty-props)

                   (fn?      n) (fn->string      n)
                   (float?   n) (float->string   n)
                   (integer? n) (integer->string n)
                   (nil?     n) (nil->string     n)
                   (string?  n) (string->string  n)
                   (var?     n) (var->string     n)
                   :else        (str             n)))]

         ; mixed->string
         (remove-unnecessary-breaks (mixed->string n {:depth       (param 0)
                                                      :wrap-items? (mixed->wrap-items? n)})))))
