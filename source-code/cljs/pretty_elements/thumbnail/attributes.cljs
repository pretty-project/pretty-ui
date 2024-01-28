
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]))

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
      (pretty-css.live/effect-attributes         thumbnail-props)
      (pretty-css.layout/indent-attributes         thumbnail-props)
      (pretty-css.control/anchor-attributes           thumbnail-props)
      (pretty-css.control/mouse-event-attributes    thumbnail-props)
      (pretty-css.basic/style-attributes          thumbnail-props)
      (pretty-css.layout/thumbnail-size-attributes thumbnail-props)

      ; i think this text attributes fn is only applied because of the text-selection property
      ; so maybe find a solution for images to make the unselectable just as the icon-attributes fn
      ; makes the icons unselectable.
      (pretty-css.content/unselectable-text-attributes thumbnail-props)))

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
      (pretty-css.basic/class-attributes        thumbnail-props)
      (pretty-css.layout/outdent-attributes      thumbnail-props)
      (pretty-css.basic/state-attributes        thumbnail-props)
      (pretty-css.appearance/theme-attributes        thumbnail-props)
      (pretty-css.layout/wrapper-size-attributes thumbnail-props)))
