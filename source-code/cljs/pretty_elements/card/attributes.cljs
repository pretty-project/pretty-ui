
(ns pretty-elements.card.attributes
    (:require [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.appearance.api  :as pretty-css.appearance]
              [pretty-css.basic.api       :as pretty-css.basic]
              [pretty-css.content.api     :as pretty-css.content]
              [pretty-css.control.api     :as pretty-css.control]
              [pretty-css.layout.api      :as pretty-css.layout]
              [pretty-css.live.api        :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-body-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [card-id card-props]
  (-> {:class :pe-card--body}
      (pretty-css.accessories/badge-attributes        card-props)
      (pretty-css.accessories/marker-attributes       card-props)
      (pretty-css.accessories/tooltip-attributes      card-props)
      (pretty-css.appearance/background-attributes    card-props)
      (pretty-css.appearance/border-attributes        card-props)
      (pretty-css.basic/style-attributes              card-props)
      (pretty-css.content/cursor-attributes           card-props)
      (pretty-css.control/anchor-attributes           card-props)
      (pretty-css.control/focus-attributes            card-props)
      (pretty-css.control/mouse-event-attributes      card-props)
      (pretty-css.control/state-attributes            card-props)
      (pretty-css.control/tab-attributes              card-props)
      (pretty-css.layout/double-block-size-attributes card-props)
      (pretty-css.layout/flex-attributes              card-props)
      (pretty-css.layout/indent-attributes            card-props)
      (pretty-css.live/effect-attributes              card-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-attributes
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ card-props]
  (-> {:class :pe-card}
      (pretty-css.appearance/theme-attributes    card-props)
      (pretty-css.basic/class-attributes         card-props)
      (pretty-css.basic/state-attributes         card-props)
      (pretty-css.layout/outdent-attributes      card-props)
      (pretty-css.layout/wrapper-size-attributes card-props)))
