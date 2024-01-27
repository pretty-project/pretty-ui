
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.appearance/border-attributes         thumbnail-props)
      (pretty-css/effect-attributes         thumbnail-props)
      (pretty-css.layout/indent-attributes         thumbnail-props)
      (pretty-css/href-attributes           thumbnail-props)
      (pretty-css/mouse-event-attributes    thumbnail-props)
      (pretty-css/style-attributes          thumbnail-props)
      (pretty-css.layout/thumbnail-size-attributes thumbnail-props)

      ; i think this text attributes fn is only applied because of the text-selection property
      ; so maybe find a solution for images to make the unselectable just as the icon-attributes fn
      ; makes the icons unselectable.
      (pretty-css/unselectable-text-attributes thumbnail-props)))

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
      (pretty-css/class-attributes        thumbnail-props)
      (pretty-css.layout/outdent-attributes      thumbnail-props)
      (pretty-css/state-attributes        thumbnail-props)
      (pretty-css/theme-attributes        thumbnail-props)
      (pretty-css.layout/wrapper-size-attributes thumbnail-props)))
