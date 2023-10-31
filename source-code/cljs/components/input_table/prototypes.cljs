
(ns components.input-table.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as table-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> table-props)))
