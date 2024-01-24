
(ns pretty-elements.card.attributes
    (:require [dom.api              :as dom]
              [pretty-css.api :as pretty-css]))

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
  [card-id {:keys [disabled?] :as card-props}]
  (-> {:class    :pe-card--body
       :disabled disabled?}
      (pretty-css/badge-attributes            card-props)
      (pretty-css/border-attributes           card-props)
      (pretty-css/color-attributes            card-props)
      (pretty-css/column-attributes           card-props)
      (pretty-css/cursor-attributes           card-props)
      (pretty-css/effect-attributes           card-props)
      (pretty-css/element-max-size-attributes card-props)
      (pretty-css/element-min-size-attributes card-props)
      (pretty-css/element-size-attributes     card-props)
      (pretty-css/indent-attributes           card-props)
      (pretty-css/href-attributes             card-props)
      (pretty-css/mouse-event-attributes      card-props)
      (pretty-css/style-attributes            card-props)
      (pretty-css/tab-attributes              card-props)))

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
      (pretty-css/class-attributes        card-props)
      (pretty-css/outdent-attributes      card-props)
      (pretty-css/state-attributes        card-props)
      (pretty-css/wrapper-size-attributes card-props)))
