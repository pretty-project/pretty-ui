
(ns elements.menu-bar.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:height (keyword)}
  ; @param (map) item-props
  ; {:active? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :href (string)(opt)
  ;  :on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-active (boolean)
  ;  :data-clickable (boolean)
  ;  :data-disabled (boolean)
  ;  :data-height (keyword)
  ;  :href (string)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [bar-id {:keys [height]} {:keys [active? disabled? href on-click] :as item-props}]
  ; XXX#9910
  ; The height of the menu bar has to set on the menu items! 
  ; This way the items' height is consistent and independent of the :orientation
  ; property of the menu bar.
  (merge (element.helpers/element-badge-attributes bar-id item-props)
         {:data-height height
          :on-mouse-up #(x.environment/blur-element! bar-id)}
         (if disabled? (cond-> {:data-disabled true}
                               (some? active?) (assoc :data-active (boolean active?)))
                       (cond-> {:data-clickable true}
                               (some? href)     (assoc :href        (str        href))
                               (some? on-click) (assoc :on-click   #(r/dispatch on-click))
                               (some? active?)  (assoc :data-active (boolean    active?))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:horizontal-align (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-horizontal-align (keyword)}
  [_ {:keys [horizontal-align]}]
  {:data-horizontal-align horizontal-align})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [bar-id {:keys [style] :as bar-props}]
  (merge (element.helpers/element-indent-attributes bar-id bar-props)
         (menu-bar-layout-attributes                bar-id bar-props)
         {:data-selectable false
          :style           style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-id bar-props]
  (merge (element.helpers/element-default-attributes bar-id bar-props)
         (element.helpers/element-outdent-attributes bar-id bar-props)))
