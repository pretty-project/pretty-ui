
(ns pretty-layouts.struct-popup.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return
  ; {}
  [_ {:keys [border-color on-cover] :as popup-props}]
  (merge {:fill-color :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (if on-cover     {:cover-color     :black})
         (-> popup-props)))
