
(ns components.item-list-header.helpers
    (:require [fruits.css.api :as css]
              [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:border (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-border (keyword)
  ;  :style (map)}
  [_ {:keys [border template]}]
  {:data-border border
   :style {:grid-template-columns template}})

(defn cell-attributes
  ; @param (map) cell-props
  ; {:on-click (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)}
  [{:keys [on-click]}]
  (if on-click {:data-click-effect :opacity
                :on-click #(r/dispatch on-click)}))
