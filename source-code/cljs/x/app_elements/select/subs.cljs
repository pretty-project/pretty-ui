
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-option-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:options-path (vector)}
  ; @param (string) new-option
  ;
  ; @return (map)
  [db [_ _ {:keys [options-path get-label-f]} new-option]]
  ; Megvizsgálja, hogy a paraméterként átadott new-option string megegyezik-e
  ; az options-path adatbázis útvonalon tárolt vektor bármelyik elemével,
  ; az elemen alkalmazott get-label-f függvény használatával.
  (let [options (get-in db options-path)]
       (letfn [(f [option] (= new-option (get-label-f option)))]
              (some f options))))
