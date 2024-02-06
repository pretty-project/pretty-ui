
(ns pretty-elements.surface.attributes
    (:require [pretty-css.appearance.api             :as pretty-css.appearance]
              [pretty-css.basic.api                  :as pretty-css.basic]
              [pretty-css.layout.api                 :as pretty-css.layout]
              [pretty-css.live.api                 :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [surface-id surface-props]
  (-> {:class :pe-surface--body}
      (pretty-css.appearance/background-attributes    surface-props)
      (pretty-css.appearance/border-attributes        surface-props)
      (pretty-css.basic/style-attributes              surface-props)
      (pretty-css.layout/double-block-size-attributes surface-props)
      (pretty-css.layout/indent-attributes            surface-props)
      (pretty-css.live/animation-attributes           surface-props)))

(defn surface-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ surface-props]
  (-> {:class :pe-surface}
      (pretty-css.appearance/theme-attributes    surface-props)
      (pretty-css.basic/class-attributes         surface-props)
      (pretty-css.basic/state-attributes         surface-props)
      (pretty-css.layout/layer-attributes        surface-props)
      (pretty-css.layout/outdent-attributes      surface-props)
      (pretty-css.layout/position-attributes     surface-props)
      (pretty-css.layout/wrapper-size-attributes surface-props)))
