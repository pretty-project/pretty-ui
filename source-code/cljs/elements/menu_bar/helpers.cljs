
(ns elements.menu-bar.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:active? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword)(opt)
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
  [bar-id _ {:keys [active? disabled? height href on-click]}]
  ; XXX#9910
  ; A menu-bar magasságát a menü elemeken szükséges alkalmazni,
  ; így {:orientation :horizontal} és {:orientation :vertical}
  ; beállítással használva egyaránt a menü elemek érzékelő területének
  ; magasságát szabályozza!
  (if disabled? (cond-> {:data-disabled true
                         :data-height   height
                         :on-mouse-up  #(element.side-effects/blur-element! bar-id)}
                        (some? active?) (assoc :data-active (boolean active?)))
                (cond-> {:data-clickable true
                         :data-height    height
                         :on-mouse-up   #(element.side-effects/blur-element! bar-id)}
                        (some? href)     (assoc :href        (str        href))
                        (some? on-click) (assoc :on-click   #(r/dispatch on-click))
                        (some? active?)  (assoc :data-active (boolean    active?)))))
