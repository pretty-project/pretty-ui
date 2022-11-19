
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.line-diagram.helpers
    (:require [elements.element.helpers :as element.helpers]
              [css.api                  :as css]
              [math.api                 :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props->total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @return (integer)
  [{:keys [sections]}]
  (letfn [(f [total-value {:keys [value]}] (+ total-value value))]
         (reduce f 0 sections)))

(defn section-props->value-ratio
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:total-value (integer)}
  ; @param (map) section-props
  ;  {:value (integer)}
  ;
  ; @return (integer)
  [{:keys [total-value]} {:keys [value]}]
  (math/percent total-value value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-section-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {data-color (keyword)}
  ;   :style (map)
  ;    {:width (string)}}
  [_ diagram-props {:keys [color] :as section-props}]
  (let [value-ratio (section-props->value-ratio diagram-props section-props)]
       {:data-color color
        :style      {:width (css/percent value-ratio)}}))

(defn diagram-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn diagram-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [diagram-id diagram-props]
  (merge (element.helpers/element-default-attributes diagram-id diagram-props)
         (element.helpers/element-indent-attributes  diagram-id diagram-props)))
