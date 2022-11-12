
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.circle-diagram.prototypes
    (:require [candy.api                       :refer [param]]
              [elements.circle-diagram.helpers :as circle-diagram.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:strength (integer)(opt)}
  ;
  ; @return (map)
  ;  {:diameter (px)
  ;   :strength (px)}
  [{:keys [strength] :as diagram-props}]
  (merge {:diameter 48
          :strength  2}
         (circle-diagram.helpers/diagram-props<-total-value diagram-props)))
