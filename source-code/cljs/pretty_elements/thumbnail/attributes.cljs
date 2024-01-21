
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-body-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [background-size disabled?] :as thumbnail-props}]
  (-> (if disabled? {:class                :pe-thumbnail--body
                     :disabled             true
                     :data-background-size background-size}
                    {:class                :pe-thumbnail--body
                     :data-background-size background-size})
      (pretty-build-kit/border-attributes         thumbnail-props)
      (pretty-build-kit/effect-attributes         thumbnail-props)
      (pretty-build-kit/indent-attributes         thumbnail-props)
      (pretty-build-kit/link-attributes           thumbnail-props)
      (pretty-build-kit/mouse-event-attributes    thumbnail-props)
      (pretty-build-kit/style-attributes          thumbnail-props)
      (pretty-build-kit/thumbnail-size-attributes thumbnail-props)
      (pretty-build-kit/unselectable-attributes   thumbnail-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail}
      (pretty-build-kit/class-attributes        thumbnail-props)
      (pretty-build-kit/outdent-attributes      thumbnail-props)
      (pretty-build-kit/state-attributes        thumbnail-props)
      (pretty-build-kit/wrapper-size-attributes thumbnail-props)))
