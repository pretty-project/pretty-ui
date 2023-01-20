
(ns elements.card.attributes
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-card-body-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [card-id {:keys [disabled? horizontal-align on-click stretch-orientation style] :as card-props}]
  (-> (if disabled? {:class                        :e-card--body
                     :data-horizontal-column-align horizontal-align
                     :data-stretch-orientation     stretch-orientation
                     :disabled                     true
                     :style                        style}
                    {:class                        :e-card--body
                     :data-click-effect            :opacity
                     :data-horizontal-column-align horizontal-align
                     :data-stretch-orientation     stretch-orientation
                     :style                        style
                     :on-click                     #(r/dispatch on-click)
                     :on-mouse-up                  #(x.environment/blur-element! card-id)})
      (pretty-css/badge-attributes            card-props)
      (pretty-css/border-attributes           card-props)
      (pretty-css/color-attributes            card-props)
      (pretty-css/element-max-size-attributes card-props)
      (pretty-css/element-min-size-attributes card-props)
      (pretty-css/indent-attributes           card-props)))

(defn static-card-body-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [card-id {:keys [horizontal-align stretch-orientation style] :as card-props}]
  (-> {:class                        :e-card--body
       :data-horizontal-column-align horizontal-align
       :data-stretch-orientation     stretch-orientation
       :style                        style}
      (pretty-css/badge-attributes            card-props)
      (pretty-css/border-attributes           card-props)
      (pretty-css/color-attributes            card-props)
      (pretty-css/element-max-size-attributes card-props)
      (pretty-css/element-min-size-attributes card-props)
      (pretty-css/indent-attributes           card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; @ignore
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
