
(ns pretty-elements.card.attributes
    (:require [dom.api        :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-body-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [card-id {:keys [disabled? style] :as card-props}]
  (-> {:class    :pe-card--body
       :disabled disabled?
       :style    style}
      (pretty-build-kit/badge-attributes            card-props)
      (pretty-build-kit/border-attributes           card-props)
      (pretty-build-kit/color-attributes            card-props)
      (pretty-build-kit/column-attributes           card-props)
      (pretty-build-kit/cursor-attributes           card-props)
      (pretty-build-kit/effect-attributes           card-props)
      (pretty-build-kit/element-max-size-attributes card-props)
      (pretty-build-kit/element-min-size-attributes card-props)
      (pretty-build-kit/element-size-attributes     card-props)
      (pretty-build-kit/indent-attributes           card-props)
      (pretty-build-kit/link-attributes             card-props)
      (pretty-build-kit/mouse-event-attributes      card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ card-props]
  (-> {:class :pe-card}
      (pretty-build-kit/class-attributes        card-props)
      (pretty-build-kit/outdent-attributes      card-props)
      (pretty-build-kit/state-attributes        card-props)
      (pretty-build-kit/wrapper-size-attributes card-props)))
