
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.sample
    (:require [mid-fruits.candy   :refer [return]]
              [x.app-elements.api :as elements]))



;; -- Bővíthető opció lista ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- my-extendable-select
  []
  ; Az {:extendable? true} beállítással használt select elem választható
  ; elemek listája bővíthető egy a lista felső részén megjelenő szöveges mező
  ; használatával.
  ;
  ; Az {:option-field-placeholder "..."} tulajdonság értéke jelenik meg a szöveges mező
  ; kitöltő szövegeként.
  ;
  ; Az {:add-option-f ...} tulajdonságként átadott függvény alkalmazásával kerül
  ; hozzáadásra az új választható opció.
  [elements/select ::my-extendable-select
                   {:add-option-f            #(return {:value %})
                    :extendable?              true
                    :option-field-placeholder "New my item"
                    :option-label-f           :value
                    :option-value-f           return}])
