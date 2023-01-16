
(ns elements.card.attributes
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [card-id {:keys [disabled? horizontal-align min-width on-click stretch-orientation style] :as card-props}]
  (-> (if disabled? {:class                        :e-card--body
                     :data-element-min-width       min-width
                     :data-horizontal-column-align horizontal-align
                     :data-stretch-orientation     stretch-orientation
                     :disabled                     true
                     :style                        style}
                    {:class                        :e-card--body
                     :data-click-effect            :opacity
                     :data-element-min-width       min-width
                     :data-horizontal-column-align horizontal-align
                     :data-stretch-orientation     stretch-orientation
                     :style                        style
                     :on-click                     #(r/dispatch on-click)
                     :on-mouse-up                  #(x.environment/blur-element! card-id)})
      (pretty-css/badge-attributes  card-props)
      (pretty-css/border-attributes card-props)
      (pretty-css/color-attributes  card-props)
      (pretty-css/indent-attributes card-props)))

(defn static-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [card-id {:keys [horizontal-align min-width stretch-orientation style] :as card-props}]
  (-> {:class                        :e-card--body
       :data-element-min-width       min-width
       :data-horizontal-column-align horizontal-align
       :data-stretch-orientation     stretch-orientation
       :style                        style}
      (pretty-css/badge-attributes  card-props)
      (pretty-css/border-attributes card-props)
      (pretty-css/color-attributes  card-props)
      (pretty-css/indent-attributes card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ card-props]
  (-> {:class :e-card}
      (pretty-css/default-attributes card-props)
      (pretty-css/outdent-attributes card-props)))
