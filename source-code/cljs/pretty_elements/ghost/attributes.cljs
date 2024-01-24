
(ns pretty-elements.ghost.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/border-attributes ghost-props)
      (pretty-css/indent-attributes ghost-props)
      (pretty-css/style-attributes  ghost-props)
      ; The ghost element uses ...
      ; ... block height profiles,
      ; ... element width profiles.
      (pretty-css/block-size-attributes   (dissoc ghost-props :width))
      (pretty-css/element-size-attributes (dissoc ghost-props :height))))

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
      (pretty-css/outdent-attributes      ghost-props)
      (pretty-css/state-attributes        ghost-props)
      (pretty-css/wrapper-size-attributes ghost-props)))
