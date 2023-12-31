
(ns pretty-elements.expandable.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (map) expandable-props
  ; {:icon (keyword)}
  ;
  ; @return (map)
  ; {:expanded? (boolean)
  ;  :icon-family (keyword)}
  [{:keys [icon] :as expandable-props}]
  (merge {:expanded? false}
         (if icon {:icon-family :material-symbols-outlined})
         (-> expandable-props)))
