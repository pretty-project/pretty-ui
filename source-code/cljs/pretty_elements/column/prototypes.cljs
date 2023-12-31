
(ns pretty-elements.column.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
