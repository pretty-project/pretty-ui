
(ns pretty-elements.card.attributes
    (:require [dom.api              :as dom]
              [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.accessories/badge-attributes     card-props)
      (pretty-css.appearance/background-attributes card-props)
      (pretty-css.appearance/border-attributes     card-props)
      (pretty-css/column-attributes       card-props)
      (pretty-css/cursor-attributes       card-props)
      (pretty-css/effect-attributes       card-props)
      (pretty-css.layout/element-size-attributes card-props)
      (pretty-css.layout/indent-attributes       card-props)
      (pretty-css/href-attributes         card-props)
      (pretty-css/mouse-event-attributes  card-props)
      (pretty-css/style-attributes        card-props)
      (pretty-css/tab-attributes          card-props)))

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
      (pretty-css.layout/outdent-attributes      card-props)
      (pretty-css/state-attributes        card-props)
      (pretty-css/theme-attributes        card-props)
      (pretty-css.layout/wrapper-size-attributes card-props)))
