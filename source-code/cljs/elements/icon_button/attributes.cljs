
(ns elements.icon-button.attributes
    (:require [css.api                    :as css]
              [elements.button.attributes :as button.attributes]
              [math.api                   :as math]
              [pretty-css.api             :as pretty-css]
              [x.components.api           :as x.components]))

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
       {:class :e-icon-button--progress-circle
        :style {:cx               12
                :cy               12
                :r                11
                :stroke-width     2
                :stroke           "var( --border-color-primary )"
                :stroke-dasharray stroke-dasharray
                :transition       transition}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ button-props]
  (-> {:class :e-icon-button--icon}
      (pretty-css/icon-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-label-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class              :e-icon-button--label
   :data-color         :default
   :data-font-weight   :bold
   :data-text-overflow :ellipsis})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:height (keyword)
  ;  :label (metamorphic-content)(opt)
  ;  :width (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-block-height (keyword)
  ;  :data-block-min-width (keyword)
  ;  :data-labeled (boolean)}
  [button-id {:keys [height label width] :as button-props}]
  ; XXX#0990
  ; By using the :data-block-min-width preset instead of using the :data-block-width
  ; preset, the icon-button element can expands horizontaly when its label doesn't
  ; fit into it.
  (-> (button.attributes/button-body-attributes button-id button-props)
      (merge {:class                 :e-icon-button--body
              :data-block-height     height
              :data-block-min-width  width
              :data-labeled          (boolean label)})
      (pretty-css/indent-attributes  button-props)
      (pretty-css/tooltip-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :e-icon-button}
      (pretty-css/default-attributes button-props)
      (pretty-css/outdent-attributes button-props)))
