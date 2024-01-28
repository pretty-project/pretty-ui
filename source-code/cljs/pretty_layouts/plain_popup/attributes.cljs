
(ns pretty-layouts.plain-popup.attributes
    (:require [re-frame.api         :as r]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.appearance.api :as pretty-css.appearance]))

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
      (pretty-css.basic/style-attributes popup-props)
      (pretty-css.appearance/theme-attributes popup-props)))

      ; class-attributes?
