
(ns templates.item-lister.menu.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:group-icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:group-icon-family (keyword)}
  [_ {:keys [group-icon] :as menu-props}]
  (merge (if group-icon {:group-icon-family :material-symbols-outlined})
         (param menu-props)))
