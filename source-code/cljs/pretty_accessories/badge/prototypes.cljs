
(ns pretty-accessories.badge.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})
            (pretty-properties/default-text-props {:text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-props-prototype
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  [badge-id badge-props]
  (let [label-props-prototype-f (fn [_] (label-props-prototype badge-id badge-props))]
       (-> badge-props (pretty-properties/default-outer-position-props {:outer-position :br :outer-position-method :absolute})
                       (pretty-properties/default-outer-size-props     {:outer-size-unit :quarter-block})
                       (pretty-standards/standard-animation-props)
                       (pretty-standards/standard-border-props)
                       (pretty-standards/standard-inner-position-props)
                       (pretty-standards/standard-inner-size-props)
                       (pretty-standards/standard-outer-position-props)
                       (pretty-standards/standard-outer-size-props)
                       (pretty-rules/apply-auto-border-crop)
                      ;(pretty-rules/auto-disable-highlight-color)
                      ;(pretty-rules/auto-disable-hover-color)
                       (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
