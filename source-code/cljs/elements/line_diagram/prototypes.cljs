
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.line-diagram.prototypes
    (:require [candy.api                     :refer [param]]
              [elements.line-diagram.helpers :as line-diagram.helpers]
              [math.api                      :as math]))
                    


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn section-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) section-props
  ;
  ; @return (map)
  ;  {:color (keyword or string)}
  [section-props]
  (merge {:color :primary}
         (param section-props)))

(defn diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:strength (integer)(opt)}
  ;
  ; @return (map)
  ;  {:strength (px)
  ;   :total-value (integer)}
  [{:keys [strength] :as diagram-props}]
  (merge {:strength 2
          :total-value (line-diagram.helpers/diagram-props->total-value diagram-props)}
         (param diagram-props)
         (if strength {:strength (math/between! strength 1 6)})))
