
(ns elements.chip.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))


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
  [_ {:keys [color fill-color style]}]
  (-> {:style style}
      (pretty-css/apply-color :color      :data-color      color)
      (pretty-css/apply-color :fill-color :data-fill-color fill-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:disabled? (boolean)(opt)
  ;  :primary-button-event (metamorphic-event)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [chip-id {:keys [disabled? primary-button-event]}]
  (if disabled? {:disabled          true}
                {:data-click-effect :opacity
                 :on-click          #(r/dispatch primary-button-event)
                 :on-mouse-up       #(x.environment/blur-element! chip-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-selectable (boolean)}
  [chip-id {:keys [on-click] :as chip-props}]
  (merge (pretty-css/indent-attributes  chip-props)
         (chip-style-attributes chip-id chip-props)
         {:data-selectable false}
         (if on-click {:data-click-effect :opacity})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [_ chip-props]
  (merge (pretty-css/default-attributes chip-props)
         (pretty-css/outdent-attributes chip-props)))
