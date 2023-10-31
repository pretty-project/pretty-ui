
(ns components.compact-list-header.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color hide-button order-button search-field] :as header-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> header-props)
         {:hide-button (merge {:border-radius    {:all :s}
                               :hover-color      :highlight
                               :icon             :menu_open
                               :tooltip-content  :hide-sidebar!
                               :tooltip-position :left}
                              (-> hide-button))}
         {:order-button (merge {:border-radius    {:all :s}
                                :hover-color      :highlight
                                :icon             :sort
                                :tooltip-content  :item-order
                                :tooltip-position :right}
                               (-> order-button))}
         {:search-field (merge {:border-color  :highlight
                                :border-radius {:all :l}
                                :font-size     :s}
                               (-> search-field))}))
