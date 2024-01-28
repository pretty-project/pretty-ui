
(ns pretty-elements.ghost.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ ghost-props]
  (-> {:class :pe-ghost--body}
      (pretty-css.live/animation-attributes ghost-props)
      (pretty-css.appearance/background-attributes     ghost-props)
      (pretty-css.appearance/border-attributes    ghost-props)
      (pretty-css.layout/indent-attributes    ghost-props)
      (pretty-css.basic/style-attributes     ghost-props)
      ; The ghost element uses ...
      ; ... block height profiles,
      ; ... element width profiles.
      (pretty-css.layout/block-size-attributes   (dissoc ghost-props :width))
      (pretty-css.layout/element-size-attributes (dissoc ghost-props :height))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ ghost-props]
  (-> {:class :pe-ghost}
      (pretty-css.basic/class-attributes        ghost-props)
      (pretty-css.layout/outdent-attributes      ghost-props)
      (pretty-css.basic/state-attributes        ghost-props)
      (pretty-css.appearance/theme-attributes        ghost-props)
      (pretty-css.layout/wrapper-size-attributes ghost-props)))
