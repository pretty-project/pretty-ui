
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [x.server-core.api :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-conflict?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) destructed-routes
  ; @param (vector) route-data
  ;  [(string) route-template
  ;   (map) route-options]
  ;
  ; @example
  ;  (engine/route-conflict? [[...] ["/my-route" {...}] [...] [...] [...]]
  ;                          ["/my-route" {...}])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [destructed-routes [route-template _]]
  (boolean (some #(= (first %) route-template)
                  (param destructed-routes))))

(defn route-template-parts-ordered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Megvizsgálja, hogy a és b route-template-part paraméter abc sorrendben került-e átadásra.
  ; A ":" karakterrel kezdődő route-template-part paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? "abcd" "abcd")
  ;  => true
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? "abcd" "efgh")
  ;  => true
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? "efgh" "abcd")
  ;  => false
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? ":abcd" "abcd")
  ;  => false
  ;
  ; @return (boolean)
  [a b]
        ; Both a and b are path-param-id identifiers.
  (cond (and (string/starts-with? a ":")
             (string/starts-with? b ":"))
        (string/abc? a b)
        ; Only a is path-param-id identifiers.
        (string/starts-with? a ":")
        (return false)
        ; Only b is path-param-id identifiers.
        (string/starts-with? b ":")
        (return true)
        ; Both a and b are generic strings.
        :else
        (string/abc? a b)))

(defn route-templates-ordered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Megvizsgálja, hogy a és b route-template paraméter abc sorrendben került-e átadásra.
  ; A ":" karakterrel kezdődő route-template paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/ab/cd" "/ab/cd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/ab/cd" "/ef/gh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/ef/gh" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/:ab/cd" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b]
  (let [a-parts (string/split a #"/")
        b-parts (string/split b #"/")]
       (vector/compared-items-ordered? a-parts b-parts route-template-parts-ordered?)))
