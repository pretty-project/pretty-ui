
(ns pretty-elements.menu-bar.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @ignore
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;
  ; @return (map)
  [_ item-props]
  (-> item-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-id bar-props]
  (-> bar-props (pretty-properties/default-flex-props       {:orientation :horizontal :overflow :scroll})
                (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :content :outer-size-unit :double-block})
                (pretty-standards/standard-border-props)
                (pretty-standards/standard-flex-props)
                (pretty-standards/standard-inner-position-props)
                (pretty-standards/standard-inner-size-props)
                (pretty-standards/standard-outer-position-props)
                (pretty-standards/standard-outer-size-props)
                (pretty-rules/apply-auto-border-crop)
                (pretty-rules/auto-align-scrollable-flex)))
               ;(pretty-rules/auto-disable-highlight-color)
               ;(pretty-rules/auto-disable-hover-color)
