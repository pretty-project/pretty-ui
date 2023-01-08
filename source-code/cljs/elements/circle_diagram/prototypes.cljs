
(ns elements.circle-diagram.prototypes
    (:require [elements.circle-diagram.helpers :as circle-diagram.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ; {:strength (integer)(opt)}
  ;
  ; @return (map)
  ; {:diameter (px)
  ;  :strength (px)}
  [{:keys [strength] :as diagram-props}]
  ; XXX#1218 (source-code/cljs/elements/circle_diagram/helpers.cljs)
  (merge {:diameter 48
          :strength  2}
         (circle-diagram.helpers/diagram-props<-total-value diagram-props)))
