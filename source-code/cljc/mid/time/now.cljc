
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns time.now
    (:require [mid-fruits.candy :refer [return]]))

            

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed
  ; @return (ms)
  [] ; TODO ...
     ; The returned value represents the time elapsed since the application's lifetime.
  #?(:clj  (return 0)
     ; The returned value represents the time elapsed since the document's lifetime.
     :cljs (.now js/performance)))
