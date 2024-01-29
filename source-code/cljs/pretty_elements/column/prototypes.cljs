
(ns pretty-elements.column.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color] :as column-props}]
  (merge {:horizontal-align    :center
          :vertical-align      :top}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> column-props)))
