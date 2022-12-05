
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.vertical-line.helpers
    (:require [css.api                  :as css]
              [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ; {:color (keyword or string)
  ;  :strength (px)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)
  ;   {:height (string)}}
  [_ {:keys [color strength style] :as line-props}]
  (-> {:style style}
      (element.helpers/apply-dimension :height           :data-height (css/px strength))
      (element.helpers/apply-color     :background-color :data-color          color)))

(defn line-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;
  ; @return (map)
  [line-id line-props]
  (merge (element.helpers/element-default-attributes line-id line-props)
         (element.helpers/element-indent-attributes  line-id line-props)))
