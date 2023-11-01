
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-diagrams.circle-diagram.utils :as circle-diagram.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:strength (integer)(opt)}
  ;
  ; @return (map)
  ; {:diameter (px)
  ;  :strength (px)}
  [{:keys [strength] :as diagram-props}]
  ; XXX#1218 (source-code/cljs/pretty_diagrams/circle_diagram/utils.cljs)
  (merge {:diameter 48
          :strength  2}
         (circle-diagram.utils/diagram-props<-total-value diagram-props)))
