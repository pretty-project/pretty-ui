
(ns pretty-elements.column.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :horizontal-align (keyword)
  ;  :vertical-align (keyword)}
  [{:keys [border-color] :as column-props}]
  (merge {:horizontal-align :center
          :vertical-align   :top
          :content-value-f return
          :placeholder-value-f return}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> column-props)))
