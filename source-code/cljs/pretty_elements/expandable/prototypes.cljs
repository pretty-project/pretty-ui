
(ns pretty-elements.expandable.prototypes
    (:require [pretty-elements.expandable.side-effects :as expandable.side-effects]
              [pretty-properties.api                   :as pretty-properties]
              [pretty-standards.api                   :as pretty-standards]
              [pretty-rules.api                   :as pretty-rules]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:button (map)(opt)
  ;  :expanded? (boolean)(opt)
  ;  ...}
  ;
  ; @return (map)
  [expandable-id {:keys [button expanded?] :as expandable-props}]
  (if expanded? (let [on-click-f (fn [] (expandable.side-effects/collapse-content! expandable-id))]
                     (-> {:on-click-f on-click-f :icon :expand_less :icon-position :right} (merge button)))
                (let [on-click-f (fn [] (expandable.side-effects/expand-content! expandable-id))]
                     (-> {:on-click-f on-click-f :icon :expand_more :icon-position :right} (merge button)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  [_ expandable-props]
  (-> expandable-props (pretty-properties/default-expandable-props {:expanded? true})
                       (pretty-properties/default-font-props       {:font-size :s :font-weight :normal})
                       (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
                       (pretty-properties/default-text-props       {:text-selectable? true})
                       (pretty-standards/standard-border-props)
                       (pretty-standards/standard-font-props)
                       (pretty-standards/standard-inner-position-props)
                       (pretty-standards/standard-inner-size-props)
                       (pretty-standards/standard-outer-position-props)
                       (pretty-standards/standard-outer-size-props)
                       (pretty-standards/standard-text-props)
                      ;(pretty-rules/auto-disable-highlight-color)
                      ;(pretty-rules/auto-disable-hover-color)
                       (pretty-rules/compose-content)))
