
(ns components.list-item-cell.helpers
    (:require [css.api      :as css]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-attributes
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [on-click]}]
  (if on-click {:data-click-effect :opacity
                :on-click #(r/dispatch on-click)}))
