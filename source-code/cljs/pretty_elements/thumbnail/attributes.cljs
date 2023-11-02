
(ns pretty-elements.thumbnail.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

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
                     :on-click             #(r/dispatch on-click)
                     :on-mouse-up          #(dom/blur-active-element!)})
      (pretty-css/border-attributes         thumbnail-props)
      (pretty-css/indent-attributes         thumbnail-props)
      (pretty-css/link-attributes           thumbnail-props)
      (pretty-css/thumbnail-size-attributes thumbnail-props)))

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
      (pretty-css/default-attributes thumbnail-props)
      (pretty-css/outdent-attributes thumbnail-props)))
