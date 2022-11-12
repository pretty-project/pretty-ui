
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.route-handler.helpers
    (:require [candy.api         :refer [return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-conflict?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) destructed-routes
  ; @param (string) route-template
  ;
  ; @example
  ;  (route-conflict? [[...]
  ;                    ["/my-route" {...}]
  ;                    [...]]
  ;                   "/my-route")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [destructed-routes route-template]
  ; Ha két útvonal {:route-template "..."} tulajdonságának értéke megegyezik, akkor az útvonal-konfliktusnak számít!
  (letfn [(f [%] (= (first %) route-template))]
         (some f destructed-routes)))

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
  ;  (route-template-parts-ordered? "abcd" "abcd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-template-parts-ordered? "abcd" "efgh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-template-parts-ordered? "efgh" "abcd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (route-template-parts-ordered? ":abcd" "abcd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b] ; Both a and b are path-param keys.
  (cond (and (string/starts-with? a ":")
             (string/starts-with? b ":"))
        (string/abc? a b)
        ; Only a is path-param keys.
        (string/starts-with? a ":")
        (return false)
        ; Only b is path-param keys.
        (string/starts-with? b ":")
        (return true)
        ; Both a and b are generic strings.
        :return
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
  ;  (route-templates-ordered? "/ab/cd" "/ab/cd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-templates-ordered? "/ab/cd" "/ef/gh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-templates-ordered? "/ef/gh" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (route-templates-ordered? "/:ab/cd" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b]
  (let [a-parts (string/split a #"/")
        b-parts (string/split b #"/")]
       (vector/compared-items-ordered? a-parts b-parts route-template-parts-ordered?)))

(defn destructed-routes->ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) routes
  ;
  ; @example
  ;  (destructed-routes->ordered-routes [["/my-route/:a" {...}]
  ;                                      ["/my-route/c"  {...}]
  ;                                      ["/my-route/b"  {...}]
  ;                                      ["/my-route"    {...}]]
  ;  =>
  ;  [["/my-route/b"  {...}]]
  ;   ["/my-route/c"  {...}]
  ;   ["/my-route/:a" {...}]
  ;   ["/my-route"    {...}]]
  ;
  ; @return (vectors in vector)
  [destructed-routes]
  (vector/sort-items-by destructed-routes route-templates-ordered? first))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn variable-route-string?
  ; @param (string) route-string
  ;
  ; @example
  ;  (variable-route-string? "/@app-home/your-route")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [route-string]
  (string/contains-part? route-string "/@app-home"))

(defn resolve-variable-route-string
  ; @param (string) route-string
  ; @param (string) app-home
  ;
  ; @example
  ;  (resolve-variable-route-string "/@app-home/your-route" "/my-app")
  ;  =>
  ;  "/my-app/your-route"
  ;
  ; @return (boolean)
  [route-string app-home]
  (string/replace-part route-string "/@app-home" (string/starts-with! app-home "/")))
