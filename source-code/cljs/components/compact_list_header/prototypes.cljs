
(ns components.compact-list-header.prototypes
    (:require [candy.api :refer [param]]))

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
         (param header-props)
         {:hide-button (merge {:border-radius    :s
                               :hover-color      :highlight
                               :icon             :menu_open
                               :tooltip          :hide-sidebar!
                               :tooltip-position :left}
                              (param hide-button))}
         {:order-button (merge {:border-radius :s
                                :hover-color   :highlight
                                :icon          :sort
                                :tooltip       :item-order}
                               (param order-button))}
         {:search-field (merge {:border-color  :highlight
                                :border-radius :l
                                :font-size     :s}
                               (param search-field))}))
