
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.sample
    (:require [candy.api    :refer [return]]
              [elements.api :as elements]))



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
