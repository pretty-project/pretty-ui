
(ns pretty-elements.dropdown-menu.prototypes
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]
              [pretty-properties.api                      :as pretty-properties]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-bar (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [menu-id {:keys [menu-bar]}]
  (-> menu-bar (assoc-in [:menu-item-default :dropdown-menu-id] menu-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:expandable (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [expandable]}]
  (-> expandable (pretty-properties/default-expandable-props {:expanded? false})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :double-block :outer-width :parent})
                 (pretty-properties/default-position-props   {:layer :uppermost :position-method :absolute})))

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
       (-> menu-props (pretty-properties/default-mouse-event-props {:on-mouse-leave-f on-mouse-leave-f})
                      (pretty-standards/standard-inner-size-props)
                      (pretty-standards/standard-outer-size-props))))
