
(ns elements.toggle.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) toggle-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [marker-color] :as toggle-props}]
  (merge (if marker-color {:marker-position :tr})
         (param toggle-props)))
