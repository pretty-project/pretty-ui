
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.multi-field.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-input-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:max-input-count (integer)
  ;  :value-path (vector)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [max-input-count value-path]}]]
  (let [group-value (get-in db value-path)
        input-count (count group-value)]
       (>= input-count max-input-count)))

(defn get-group-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ;
  ; @return (strings in vector)
  [db [_ _ {:keys [value-path]}]]
  (let [group-value (get-in db value-path)]
       (if (empty? group-value)
           [nil] group-value)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.multi-field/get-group-value get-group-value)
