
(ns pretty-elements.dropdown-menu.prototypes
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]
              [pretty-properties.api                      :as pretty-properties]
              [pretty-rules.api                           :as pretty-rules]
              [pretty-standards.api                       :as pretty-standards]
              [pretty-subitems.api                        :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) menu-bar
  ;
  ; @return (map)
  [id _ menu-bar]
  (-> menu-bar (assoc-in [:menu-item-default :dropdown-menu-id] id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) expandable
  ;
  ; @return (map)
  [_ _ expandable]
  (-> expandable (pretty-properties/default-lifecycle-props      {:mounted? false})
                 (pretty-properties/default-outer-position-props {:outer-layer :uppermost :outer-position-method :absolute})
                 (pretty-properties/default-outer-size-props     {:outer-size-unit :double-block :outer-width :parent})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [on-mouse-leave-f       (fn [_] (dropdown-menu.side-effects/on-mouse-leave-f id props))
        expandable-prototype-f (fn [%] (expandable-prototype                        id props %))
        menu-bar-prototype-f   (fn [%] (menu-bar-prototype                          id props %))]
       (-> props (pretty-properties/default-mouse-event-props {:on-mouse-leave-f on-mouse-leave-f})
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/subitem<-disabled-state :menu-bar :expandable)
                 (pretty-subitems/leave-disabled-state    :menu-bar :expandable)
                 (pretty-subitems/apply-subitem-prototype :expandable expandable-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :menu-bar   menu-bar-prototype-f))))
