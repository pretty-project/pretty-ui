
(ns components.popup-progress-indicator.views
    (:require [fruits.random.api   :as random]
              [pretty-elements.api :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-progress-label
  ; @param (keyword) indicator-id
  ; @param (map) indicator-props
  ; {:color (keyword)(opt)
  ;  :label (metamorphic-content)}
  [_ {:keys [color label]}]
  [pretty-elements/label ::popup-progress-label
                         {:color   (or color :muted)
                          :content label}])

(defn- popup-progress-indicator
  ; @param (keyword) indicator-id
  ; @param (map) indicator-props
  ; {:color (keyword)(opt)
  ;  :indent (map)
  ;  :label (metamorphic-content)}
  [indicator-id {:keys [indent] :as indicator-props}]
  [pretty-elements/column ::popup-progress-indicator
                          {:content          [popup-progress-label indicator-id indicator-props]
                           :horizontal-align :center
                           :indent           indent
                           :vertical-align   :center}])

(defn view
  ; @param (keyword)(opt) indicator-id
  ; @param (map) indicator-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (string)(opt)
  ;   Default: "var( --fill-color-default )"
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;   :hidden, :visible
  ;   Default: :visible
  ;  :query (vector)(opt)
  ;  :refresh-interval (ms)(opt)
  ;   W/ {:query ...}
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [popup-progress-indicator {...}]
  ;
  ; @usage
  ; [popup-progress-indicator :my-popup-progress-indicator {...}]
  ([indicator-props]
   [view (random/generate-keyword) indicator-props])

  ([indicator-id indicator-props]
   ; @note (tutorials#parameterizing)
   (fn [_ indicator-props]
       (let [] ; indicator-props (popup-progress-indicator.prototypes/indicator-props-prototype indicator-props)
            [popup-progress-indicator indicator-id indicator-props]))))
