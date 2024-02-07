
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

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
      (pretty-attributes/border-attributes         thumbnail-props)
      (pretty-attributes/effect-attributes         thumbnail-props)
      (pretty-attributes/indent-attributes         thumbnail-props)
      (pretty-attributes/anchor-attributes           thumbnail-props)
      (pretty-attributes/focus-attributes         thumbnail-props)
      (pretty-attributes/mouse-event-attributes    thumbnail-props)
      (pretty-attributes/cursor-attributes       thumbnail-props)
      (pretty-attributes/clickable-state-attributes          thumbnail-props)
      (pretty-attributes/style-attributes          thumbnail-props)
      (pretty-attributes/full-block-size-attributes thumbnail-props)

      ; i think this text attributes fn is only applied because of the text-selection property
      ; so maybe find a solution for images to make them unselectable just like the icon-attributes fn
      ; makes the icons unselectable.
      (pretty-attributes/text-attributes thumbnail-props)))
      ; + :text-selectable? false

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
      (pretty-attributes/class-attributes       thumbnail-props)
      (pretty-attributes/outdent-attributes      thumbnail-props)
      (pretty-attributes/state-attributes       thumbnail-props)
      (pretty-attributes/theme-attributes        thumbnail-props)
      (pretty-attributes/wrapper-size-attributes thumbnail-props)))
