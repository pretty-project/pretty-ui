
(ns elements.card.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

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
      (element.helpers/apply-color :border-color :data-border-color border-color)
      (element.helpers/apply-color :fill-color   :data-fill-color   fill-color)
      (element.helpers/apply-color :hover-color  :data-hover-color  hover-color)))

(defn card-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:border-radius (keyword)(opt)
  ;  :horizontal-align (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-element-width (keyword)
  ;  :data-horizontal-column-align (keyword)
  ;  :data-stretch-orientation (keyword)}
  [_ {:keys [border-radius horizontal-align min-width stretch-orientation]}]
  {:data-border-radius           border-radius
   :data-element-width           min-width
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
  (merge (element.helpers/element-indent-attributes card-id card-props)
         (element.helpers/element-badge-attributes  card-id card-props)
         (card-style-attributes                     card-id card-props)
         (card-layout-attributes                    card-id card-props)
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
  (merge (element.helpers/element-indent-attributes card-id card-props)
         (card-style-attributes                     card-id card-props)
         (card-layout-attributes                    card-id card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (merge (element.helpers/element-default-attributes card-id card-props)
         (element.helpers/element-outdent-attributes card-id card-props)))
