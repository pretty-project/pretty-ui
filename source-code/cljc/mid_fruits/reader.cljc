
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.reader
    (:require #?(:cljs [cljs.reader :as reader])
              #?(:clj  [clojure.edn :as edn])
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-str
  ; @param (string) n
  ;
  ; @usage
  ;  (reader/read-str "{:a "b"}")
  ;
  ; @return (*)
  [n]
  #?(:cljs (try (reader/read-string n) (catch :default  e (str e)))
     :clj  (try (edn/read-string    n) (catch Exception e (str e)))))

(defn mixed->string
  ; @param (*) n
  ;
  ; @return (string)
  [n]
  (str n))

(defn string->mixed
  ; @param (n) string
  ;
  ; @example
  ;  (reader/string->mixed "")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (reader/string->mixed ":foo")
  ;  =>
  ;  :foo
  ;
  ; @example
  ;  (reader/string->mixed "{:foo :bar}")
  ;  =>
  ;  {:foo :bar}
  ;
  ; @example
  ;  (reader/string->mixed "[:foo]")
  ;  =>
  ;  [:foo]
  ;
  ; @return (nil, keyword, map, string or vector)
  [n]
  (if (string/nonempty? n)
      (let [x (read-str n)]
           (if (or (keyword? x)
                   (map?     x)
                   (vector?  x))
               (return x)
               ; Előfordulhat, hogy a read-str függvény egy Error objektummal tér vissza
               (return n)))
      (return nil)))

(defn string->map
  ; @param (string) n
  ;
  ; @example
  ;  (reader/string->map "foo")
  ;  =>
  ;  {:0 "foo"}
  ;
  ; @example
  ;  (reader/string->map nil)
  ;  =>
  ;  {}
  ;
  ; @example
  ;  (reader/string->map "{:foo :bar}")
  ;  =>
  ;  {:foo :bar}
  ;
  ; @return (map)
  [n]
  (if-let [x (string->mixed n)]
          (cond (map? x) x
                (nil? n) {}
                :return  {:0 (str n)})
          (return {})))
