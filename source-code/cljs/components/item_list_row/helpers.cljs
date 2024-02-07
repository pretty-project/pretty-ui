
(ns components.item-list-row.helpers
    (:require [pretty-attributes.api            :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:disabled? (boolean)(opt)
  ;  :drag-attributes (map)(opt)
  ;  :template (string)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [disabled? drag-attributes template] :as row-props}]
  (-> (if drag-attributes (-> drag-attributes (update :style merge {:grid-template-columns template}))
                          {:style {:grid-template-columns template :opacity (if disabled? ".5")}})
      (pretty-attributes/marker-attributes    row-props)
      (pretty-attributes/background-attributes row-props)
      (pretty-attributes/border-attributes     row-props)))
