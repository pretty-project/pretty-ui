
(ns elements.menu-bar.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

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
  ;  :data-block-height (keyword)
  ;  :data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :href (string)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [bar-id {:keys [height]} {:keys [active? disabled? href on-click] :as item-props}]
  ; XXX#9910
  ; The height of the menu bar has to set on the menu items!
  ; This way the items' height is consistent and independent of the :orientation
  ; property of the menu bar.
  (-> (if disabled? (cond-> {:data-block-height height
                             :data-disabled     true
                             :on-mouse-up       #(x.environment/blur-element! bar-id)}
                            (some? active?) (assoc :data-active (boolean active?)))
                    (cond-> {:data-block-height height
                             :data-click-effect :opacity
                             :on-mouse-up #(x.environment/blur-element! bar-id)}
                            (some? href)     (assoc :href (str href))
                            (some? on-click) (assoc :on-click #(r/dispatch on-click))
                            (some? active?)  (assoc :data-active (boolean active?))))
      (pretty-css/badge-attributes item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-horizontal-align (keyword)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as bar-props}]
  (-> {:data-horizontal-align horizontal-align
       :data-selectable       false
       :style                 style}
      (pretty-css/indent-attributes bar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [_ bar-props]
  (-> {} (pretty-css/default-attributes bar-props)
         (pretty-css/outdent-attributes bar-props)))
