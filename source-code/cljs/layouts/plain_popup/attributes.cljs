
(ns layouts.plain-popup.attributes
    (:require [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-cover-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [popup-id {:keys [close-by-cover? cover-color]}]
  (merge {:class           :l-plain-popup--cover
          :data-fill-color cover-color}
         (if close-by-cover? {:on-click #(r/dispatch [:x.ui/remove-popup! popup-id])})))

(defn popup-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style]}]
  {:class :l-plain-popup
   :style style})
