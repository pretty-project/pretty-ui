
(ns elements.chip.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:disabled? (boolean)(opt)
  ;  :primary-button (map)
  ;   {:on-click (metamorphic-event)}}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [chip-id {{:keys [on-click]} :primary-button :keys [disabled?]}]
  (if disabled? {:disabled          true}
                {:data-click-effect :opacity
                 :on-click          #(r/dispatch on-click)
                 :on-mouse-up       #(x.environment/blur-element! chip-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:on-click (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [chip-id {:keys [on-click style] :as chip-props}]
  (-> (if on-click {:data-click-effect :opacity
                    :data-selectable   false
                    :style             style}
                   {:data-selectable   false
                    :style             style})
      (pretty-css/color-attributes  chip-props)
      (pretty-css/indent-attributes chip-props)))

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
  (-> {} (pretty-css/default-attributes chip-props)
         (pretty-css/outdent-attributes chip-props)))
