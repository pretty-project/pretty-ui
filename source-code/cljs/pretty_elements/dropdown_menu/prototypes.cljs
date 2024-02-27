
(ns pretty-elements.dropdown-menu.prototypes
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]
              [pretty-properties.api                      :as pretty-properties]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

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
  (-> expandable (pretty-properties/default-expandable-props     {:expanded? false})
                 (pretty-properties/default-outer-position-props {:outer-layer :uppermost :outer-position-method :absolute})
                 (pretty-properties/default-outer-size-props     {:outer-size-unit :double-block :outer-width :parent})))

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
  (let [on-mouse-leave-f             (fn [_] (dropdown-menu.side-effects/on-mouse-leave-f menu-id menu-props))
        expandable-props-prototype-f (fn [_] (expandable-props-prototype                  menu-id menu-props))
        bar-props-prototype-f        (fn [_] (bar-props-prototype                         menu-id menu-props))]
       (-> menu-props (pretty-properties/default-mouse-event-props {:on-mouse-leave-f on-mouse-leave-f})
                      (pretty-standards/standard-inner-position-props)
                      (pretty-standards/standard-inner-size-props)
                      (pretty-standards/standard-outer-position-props)
                      (pretty-standards/standard-outer-size-props)
                      (pretty-subitems/subitem<-disabled-state :menu-bar :expandable)
                      (pretty-subitems/leave-disabled-state    :menu-bar :expandable)
                      (pretty-subitems/apply-subitem-prototype :expandable expandable-props-prototype-f)
                      (pretty-subitems/apply-subitem-prototype :menu-bar   bar-props-prototype-f))))
