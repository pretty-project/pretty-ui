
(ns elements.card.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:border-color (keyword or string)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :hover-color (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [border-color fill-color hover-color style]}]
  (-> {:style style}
      (pretty-css/apply-color :border-color :data-border-color border-color)
      (pretty-css/apply-color :fill-color   :data-fill-color   fill-color)
      (pretty-css/apply-color :hover-color  :data-hover-color  hover-color)))

(defn card-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:border-radius (keyword)(opt)
  ;  :border-width (keyword)(opt)
  ;  :horizontal-align (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-border-width (keyword)
  ;  :data-element-min-width (keyword)
  ;  :data-horizontal-column-align (keyword)
  ;  :data-stretch-orientation (keyword)}
  [_ {:keys [border-radius border-width horizontal-align min-width stretch-orientation]}]
  {:data-border-radius           border-radius
   :data-border-width            border-width
   :data-element-min-width       min-width
   :data-horizontal-column-align horizontal-align
   :data-stretch-orientation     stretch-orientation})

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
  ; {:data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [card-id {:keys [disabled? on-click] :as card-props}]
  (merge (pretty-css/indent-attributes   card-props)
         (pretty-css/badge-attributes    card-props)
         (card-style-attributes  card-id card-props)
         (card-layout-attributes card-id card-props)
         (if disabled? {:disabled          true}
                       {:data-click-effect :opacity
                        :on-click          #(r/dispatch on-click)
                        :on-mouse-up       #(x.environment/blur-element! card-id)})))

(defn static-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (merge (pretty-css/indent-attributes   card-props)
         (card-style-attributes  card-id card-props)
         (card-layout-attributes card-id card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [_ card-props]
  (merge (pretty-css/default-attributes card-props)
         (pretty-css/outdent-attributes card-props)))
