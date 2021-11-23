
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.08
; Description:
; Version: v0.6.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.reader
    (:require #?(:cljs [cljs.reader :as reader])
              #?(:clj  [clojure.edn :as edn])
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- read-n
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @return (*)
  [n]
  #?(:cljs (try (reader/read-string n) (catch :default  e (str "Reader error #510")))
     :clj  (try (edn/read-string    n) (catch Exception e (str "Reader error #509")))))

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
  ;  => nil
  ;
  ; @example
  ;  (reader/string->mixed ":foo")
  ;  => :foo
  ;
  ; @example
  ;  (reader/string->mixed "{:foo :bar}")
  ;  => {:foo :bar}
  ;
  ; @example
  ;  (reader/string->mixed "[:foo]")
  ;  => [:foo]
  ;
  ; @return (nil, keyword, map, string or vector)
  [n]
  (if (string/nonempty? n)
      (let [x (read-n n)]
           (if (or (keyword? x)
                   (map?     x)
                   (vector?  x))
               (return x)
               ; XXX#8709
               ; A string->mixed ha egy stringet kellene, hogy felismerjen,
               ; akkor előfordulhat, hogy Error objektummal tér vissza
               (return n)))
      (return nil)))

(defn string->map
  ; @param (string) n
  ;
  ; @example
  ;  (reader/string->map "foo")
  ;  => {:0 "foo"}
  ;
  ; @example
  ;  (reader/string->map nil)
  ;  => {}
  ;
  ; @example
  ;  (reader/string->map "{:foo :bar}")
  ;  => {:foo :bar}
  ;
  ; @return (map)
  [n]
  (let [x (string->mixed n)]
       (cond (map? x) (return x)
             (nil? n) (param  {})
             :else    (return {:0 (str n)}))))
