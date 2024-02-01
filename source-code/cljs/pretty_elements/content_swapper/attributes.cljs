
(ns pretty-elements.content-swapper.attributes
    (:require [pretty-css.appearance.api             :as pretty-css.appearance]
              [pretty-css.basic.api                  :as pretty-css.basic]
              [pretty-css.layout.api                 :as pretty-css.layout]
              [pretty-css.live.api                 :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [swapper-id swapper-props]
  (-> {:class :pe-content-swapper--body}
      (pretty-css.basic/style-attributes              swapper-props)
      (pretty-css.layout/double-block-size-attributes swapper-props)
      (pretty-css.layout/indent-attributes            swapper-props)
      (pretty-css.live/animation-attributes           swapper-props)))

(defn swapper-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ swapper-props]
  (-> {:class :pe-content-swapper}
      (pretty-css.appearance/theme-attributes    swapper-props)
      (pretty-css.basic/class-attributes         swapper-props)
      (pretty-css.basic/state-attributes         swapper-props)
      (pretty-css.layout/outdent-attributes      swapper-props)
      (pretty-css.layout/wrapper-size-attributes swapper-props)))
