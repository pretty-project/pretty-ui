
(ns components.menu-table.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [table-id {:keys [style] :as table-props}]
  (merge (component.helpers/component-indent-attributes table-id table-props)
         {:style style}))

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [table-id table-props]
  (merge (component.helpers/component-default-attributes table-id table-props)
         (component.helpers/component-outdent-attributes table-id table-props)))
