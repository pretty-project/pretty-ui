
(ns pretty-elements.column.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as column-props}]
  (merge {:horizontal-align    :center
          :vertical-align      :top}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> column-props)))
