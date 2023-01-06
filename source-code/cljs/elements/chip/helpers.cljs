
(ns elements.chip.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [chip-id {:keys [color fill-color style] :as chip-props}]
  (-> {:style style}
      (element.helpers/apply-color :color      :data-color      color)
      (element.helpers/apply-color :fill-color :data-fill-color fill-color)))

(defn chip-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [chip-id {:keys [on-click] :as chip-props}]
  (merge (element.helpers/element-indent-attributes chip-id chip-props)
         (chip-style-attributes                     chip-id chip-props)
         {:data-selectable false}
         (if on-click {:data-clickable true})))

(defn chip-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [chip-id chip-props]
  (merge (element.helpers/element-default-attributes chip-id chip-props)
         (element.helpers/element-outdent-attributes chip-id chip-props)))

(defn primary-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:disabled? (boolean)(opt)
  ;  :primary-button-event (metamorphic-event)}
  ;
  ; @return (map)
  ; {}
  [chip-id {:keys [disabled? primary-button-event] :as chip-props}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :on-click       #(r/dispatch primary-button-event)
                 :on-mouse-up    #(element.side-effects/blur-element! chip-id)}))
