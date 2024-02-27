
(ns pretty-elements.horizontal-separator.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})
            (pretty-properties/default-text-props {:text-color :muted :text-transform :uppercase :text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  [separator-id separator-props]
  (let [label-props-prototype-f (fn [_] (label-props-prototype separator-id separator-props))]
       (-> separator-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                           (pretty-properties/default-line-props       {:line-color :muted :line-orientation :horizontal :line-size :grow})
                           (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :full-block})
                           (pretty-standards/standard-flex-props)
                           (pretty-standards/standard-inner-position-props)
                           (pretty-standards/standard-inner-size-props)
                           (pretty-standards/standard-line-props)
                           (pretty-standards/standard-outer-position-props)
                           (pretty-standards/standard-outer-size-props)
                          ;(pretty-rules/auto-align-scrollable-flex)
                           (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
