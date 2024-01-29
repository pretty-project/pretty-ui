
(ns pretty-elements.expandable.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:icon (keyword)}
  ;
  ; @return (map)
  ; {:expanded? (boolean)
  ;  :icon-family (keyword)}
  [_ {:keys [icon] :as expandable-props}]
  (merge {:expanded? false}
         (if icon {:icon-family :material-symbols-outlined})
         (-> expandable-props)))
