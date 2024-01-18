
(ns pretty-elements.thumbnail.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  [_ {:keys [background-size disabled? href on-click style] :as thumbnail-props}]
  (-> (if disabled? {:class                :pe-thumbnail--body
                     :disabled             true
                     :data-background-size background-size
                     :data-selectable      false
                     :style                style}
                    {:class                :pe-thumbnail--body
                     :data-background-size background-size
                     :data-click-effect    (if (or href on-click) :opacity)
                     :data-selectable      false
                     :style                style
                     :on-click             #(pretty-build-kit/dispatch-event-handler! on-click)
                     :on-mouse-up          #(dom/blur-active-element!)})
      (pretty-build-kit/border-attributes         thumbnail-props)
      (pretty-build-kit/indent-attributes         thumbnail-props)
      (pretty-build-kit/link-attributes           thumbnail-props)
      (pretty-build-kit/thumbnail-size-attributes thumbnail-props)))

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
