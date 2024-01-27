
(ns pretty-layouts.plain-popup.attributes
    (:require [pretty-css.api :as pretty-css]
              [re-frame.api         :as r]))

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
      (pretty-css/style-attributes popup-props)
      (pretty-css/theme-attributes popup-props)))
