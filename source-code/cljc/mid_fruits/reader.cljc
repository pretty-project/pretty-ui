
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.08
; Description:
; Version: v0.9.2



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
  #?(:cljs (try (reader/read-string n) (catch :default  e (str "read-string error: " n)))
     :clj  (try (edn/read-string    n) (catch Exception e (str "read-string error: " n)))))

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
           (cond (keyword? x) x
                 (map?     x) x
                 (vector?  x) x
                 ; Előfordulhat, hogy a read-str függvény egy Error objektummal tér vissza
                 :else n))
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
                :else    {:0 (str n)})
          (return {})))