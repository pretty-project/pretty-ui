
(ns components.item-list-row.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:border (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :drag-attributes (map)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :template (string)}
  ;
  ; @return (map)
  ; {:data-border (keyword)
  ;  :data-fill-color (boolean)
  ;  :style (map)}
  [row-id {:keys [border disabled? drag-attributes highlighted? template] :as row-props}]
  (merge (component.helpers/component-marker-attributes row-id row-props)
         {:data-border     border
          :data-fill-color (if highlighted? :highlight)}
         (if drag-attributes (-> drag-attributes (update :style merge {:grid-template-columns template}))
                             {:style {:grid-template-columns template :opacity (if disabled? ".5")}})))
