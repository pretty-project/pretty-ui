
(ns pretty-layouts.plain-popup.attributes
    (:require [pretty-attributes.api :as pretty-attributes]
              [re-frame.api          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-cover-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [popup-id {:keys [cover-color on-cover]}]
  (merge {:class           :pl-plain-popup--cover
          :data-fill-color cover-color
          :on-click #(r/dispatch on-cover)}))

(defn popup-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ popup-props]
  (-> {:class :pl-plain-popup}
      (pretty-attributes/style-attributes popup-props)
      (pretty-attributes/theme-attributes popup-props)))

      ; class-attributes?
      ; + class-attributes, state-attributes, style-attributes, theme-attributes
