
(ns pretty-layouts.plain-popup.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [on-cover] :as popup-props}]
  (merge {}
         (if on-cover {:cover-color :black})
         (-> popup-props)))
; size-unit :screen
