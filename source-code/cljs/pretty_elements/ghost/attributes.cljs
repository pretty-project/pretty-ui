
(ns pretty-elements.ghost.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css/animation-attributes ghost-props)
      (pretty-css.appearance/background-attributes     ghost-props)
      (pretty-css.appearance/border-attributes    ghost-props)
      (pretty-css.layout/indent-attributes    ghost-props)
      (pretty-css/style-attributes     ghost-props)
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
      (pretty-css/class-attributes        ghost-props)
      (pretty-css.layout/outdent-attributes      ghost-props)
      (pretty-css/state-attributes        ghost-props)
      (pretty-css/theme-attributes        ghost-props)
      (pretty-css.layout/wrapper-size-attributes ghost-props)))
