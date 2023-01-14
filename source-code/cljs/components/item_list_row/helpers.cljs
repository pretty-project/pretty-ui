
(ns components.item-list-row.helpers
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/border-attributes row-props)
      (pretty-css/color-attributes  row-props)
      (pretty-css/marker-attributes row-props)))