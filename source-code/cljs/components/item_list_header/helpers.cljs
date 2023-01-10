
(ns components.item-list-header.helpers
    (:require [css.api :as css]
              [re-frame.api :as r]))

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
  ; {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)}
  [{:keys [on-click]}]
  (if on-click {:data-click-effect :opacity
                :on-click #(r/dispatch on-click)}))
