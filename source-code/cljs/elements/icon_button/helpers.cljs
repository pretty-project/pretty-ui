
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon-button.helpers
    (:require [css.api                  :as css]
              [elements.button.helpers  :as button.helpers]
              [elements.element.helpers :as element.helpers]
              [math.api                 :as math]
              [x.components.api         :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:height (keyword)
  ;  :label (metamorphic-content)(opt)
  ;  :tooltip (metamorphic-content)(opt)
  ;  :width (keyword)}
  ;
  ; @return (map)
  ; {:data-height (keyword)
  ;  :data-labeled (boolean)
  ;  :data-width (keyword)}
  [button-id {:keys [height label tooltip width] :as button-props}]
  (merge {:data-height  height
          :data-width   width
          :data-labeled (boolean label)
          :title        (x.components/content tooltip)}
         (button.helpers/button-body-attributes button-id button-props)))

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:tooltip (metamorphic-content)(opt)
  ;  :data-variant (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-tooltip (string)
  ;  :data-variant (keyword)}
  [button-id {:keys [tooltip variant] :as button-props}]
  (merge (element.helpers/element-default-attributes button-id button-props)
         (element.helpers/element-indent-attributes  button-id button-props)
         (if tooltip {:data-tooltip (x.components/content {:content tooltip})})
         (if variant {:data-variant variant})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn progress-attributes
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {:cx (px)
  ;  :cy (px)
  ;  :r (px)
  ;  :stroke (string)
  ;  :stroke-dasharray (string)
  ;  :stroke-width (px)
  ;  :transition (string)}
  [_ {:keys [progress progress-duration]}]
  ; W:  24px
  ; H:  24px
  ; Do: W                 = 24px
  ; Di: W - 2stroke-width = 20px
  ; Ro: Do / 2            = 12px
  ; Ri: Di / 2            = 10px
  ; Rc: (Do + Di) / 2     = 11px
  ; CIRCUM: 2Rc * Pi      = 69.11px
  (let [percent-result      (math/percent-result 69.11        progress)
        percent-rem         (math/percent-result 69.11 (- 100 progress))
        stroke-dasharray    (str percent-result" "percent-rem)
        transition-duration (css/ms progress-duration)
        transition          (if (> progress 0) (str "stroke-dasharray " transition-duration " linear"))]
       {:style {:cx           12
                :cy           12
                :r            11
                :stroke-width 2
                :stroke           "var( --border-color-primary )"
                :stroke-dasharray stroke-dasharray
                :transition       transition}}))
