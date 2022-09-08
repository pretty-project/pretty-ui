
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.counter.subs
    (:require [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-decreasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (boolean)
  [db [_ _ {:keys [min-value value-path]}]]
  (let [value (get-in db value-path)]
       (or (nil? min-value)
           (and (some? min-value)
                (<     min-value value)))))

(defn value-increasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (boolean)
  [db [_ _ {:keys [max-value value-path]}]]
  (let [value (get-in db value-path)]
       (or (nil? max-value)
           (and (some? max-value)
                (>     max-value value)))))
