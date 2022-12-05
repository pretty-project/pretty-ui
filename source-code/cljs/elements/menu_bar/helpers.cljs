
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.menu-bar.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:font-size (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)}
  [_ {:keys [font-size font-weight]}]
  {:data-font-size font-size})

(defn menu-bar-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:height (keyword)
  ;  :horizontal-align (keyword)(opt)
  ;  :orientation (keyword)}
  ;
  ; @return (map)
  ; {:data-height (keyword)
  ;  :data-horizontal-align (keyword)
  ;  :data-orientation (keyword)}
  [_ {:keys [height horizontal-align orientation]}]
  {:data-height           height
   :data-horizontal-align horizontal-align
   :data-orientation      orientation})

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
  (merge {:data-selectable false
          :style           style}
         (menu-bar-font-attributes   bar-id bar-props)
         (menu-bar-layout-attributes bar-id bar-props)))

(defn menu-bar-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-id bar-props]
  (merge (element.helpers/element-default-attributes bar-id bar-props)
         (element.helpers/element-indent-attributes  bar-id bar-props)))



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
  ;  :href (string)(opt)
  ;  :on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-active (boolean)
  ;  :data-clickable (boolean)
  ;  :data-disabled (boolean)
  ;  :href (string)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [bar-id _ {:keys [active? disabled? href on-click]}]
                ; If menu-item is disabled ...
  (if disabled? (cond-> {:data-disabled true
                         :on-mouse-up  #(element.side-effects/blur-element! bar-id)}
                        (some? active?) (assoc :data-active (boolean active?)))
                ; If menu-item is NOT disabled ...
                (cond-> {:data-clickable true
                         :on-mouse-up   #(element.side-effects/blur-element! bar-id)}
                        (some? href)     (assoc :href        (str        href))
                        (some? on-click) (assoc :on-click   #(r/dispatch on-click))
                        (some? active?)  (assoc :data-active (boolean    active?)))))
