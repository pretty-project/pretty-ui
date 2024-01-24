
(ns pretty-elements.row.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (map) row-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :horizontal-align (keyword)
  ;  :vertical-align (keyword)}
  [{:keys [border-color] :as row-props}]
  (merge {:horizontal-align    :left
          :vertical-align      :center}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> row-props)))
