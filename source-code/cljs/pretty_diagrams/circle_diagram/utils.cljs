
(ns pretty-diagrams.circle-diagram.utils
    (:require [fruits.css.api                        :as css]
              [fruits.math.api                       :as math]
              [pretty-css.api                        :as pretty-css]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props<-total-value
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  ;
  ; @usage
  ; (diagram-props<-total-value {:sections [{:value 50} {:value 25} {:value 50}]})
  ; =>
  ; {:sections [{:value 25 :sum 0} {:value 25 :sum 25} {:value 50 :sum 50}] :total-value 100}
  ;
  ; @return (map)
  ; {:sections (maps in vector)
  ;   [{:sum (integer)}]
  ;  :total-value (integer)}
  [{:keys [sections] :as diagram-props}]
  ; XXX#1218
  ; This function iterates over the sections (from the 'diagram-props' map) ...
  ; ... and calculates the total value of all sections.
  ; ... and calculates the previous sections summary value of each section.
  ;
  ; The diagram needs ...
  ; ... the total value to calculates how the sections related to the total.
  ; ... the previous sections summary of each section to calculates how
  ;     a section has to be rotated.
  (letfn [(f0 [{:keys [total-value] :as diagram-props} dex {:keys [value]}]
              (-> diagram-props (update   :total-value + value)
                                (assoc-in [:sections dex :sum] (or total-value 0))))]
         (reduce-kv f0 diagram-props sections)))
