
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon-button.helpers
    (:require [elements.button.helpers  :as button.helpers]
              [elements.element.helpers :as element.helpers]
              [x.components.api         :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:height (keyword)
  ;   :label (metamorphic-content)(opt)
  ;   :width (keyword)}
  ;
  ; @return (map)
  ;  {:data-height (keyword)
  ;   :data-labeled (boolean)
  ;   :data-width (keyword)}
  [button-id {:keys [height label width] :as button-props}]
  (merge {:data-height  height
          :data-width   width
          :data-labeled (boolean label)}
         (button.helpers/button-body-attributes button-id button-props)))

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:tooltip (metamorphic-content)(opt)
  ;   :data-variant (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-tooltip (string)
  ;   :data-variant (keyword)}
  [button-id {:keys [tooltip variant] :as button-props}]
  (merge (element.helpers/element-default-attributes button-id button-props)
         (element.helpers/element-indent-attributes  button-id button-props)
         (if tooltip {:data-tooltip (x.components/content {:content tooltip})})
         (if variant {:data-variant variant})))
