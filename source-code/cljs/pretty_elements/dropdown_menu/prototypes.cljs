
(ns pretty-elements.dropdown-menu.prototypes
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]
              [pretty-properties.api                      :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-bar (map)(opt)}
  ;
  ; @return (map)
  [menu-id {:keys [menu-bar]}]
  (-> menu-bar (assoc-in [:menu-item-default :menu-id] menu-id)))

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:surface (map)(opt)}
  ;
  ; @return (map)
  [_ {:keys [surface]}]
  (-> surface (pretty-properties/default-position-props {:layer :uppermost :position-method :absolute})
              (pretty-properties/default-size-props     {:width :parent})
              (pretty-properties/default-state-props    {:mounted? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [menu-id menu-props]
  (let [on-mouse-leave-f (fn [_] (dropdown-menu.side-effects/on-mouse-leave-f menu-id menu-props))]
       (-> menu-props (pretty-properties/default-mouse-event-props {:on-mouse-leave-f on-mouse-leave-f}))))
