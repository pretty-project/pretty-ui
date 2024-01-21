
(ns pretty-elements.ghost.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/border-attributes ghost-props)
      (pretty-build-kit/indent-attributes ghost-props)
      (pretty-build-kit/style-attributes  ghost-props)
      ; The ghost element uses ...
      ; ... block height profiles,
      ; ... element width profiles.
      (pretty-build-kit/block-size-attributes   (dissoc ghost-props :width))
      (pretty-build-kit/element-size-attributes (dissoc ghost-props :height))))

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
      (pretty-build-kit/class-attributes        ghost-props)
      (pretty-build-kit/outdent-attributes      ghost-props)
      (pretty-build-kit/state-attributes        ghost-props)
      (pretty-build-kit/wrapper-size-attributes ghost-props)))
