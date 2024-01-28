
(ns pretty-layouts.plain-popup.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [on-cover] :as popup-props}]
  (merge {}
         (if on-cover {:cover-color :black})
         (-> popup-props)))
