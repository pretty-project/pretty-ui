
(ns components.item-list-row.helpers
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/border-attributes row-props)
      (pretty-build-kit/color-attributes  row-props)
      (pretty-build-kit/marker-attributes row-props)))
