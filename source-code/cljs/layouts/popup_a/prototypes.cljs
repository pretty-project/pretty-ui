
(ns layouts.popup-a.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  ; {}
  ;
  ; @return
  ; {}
  [{:keys [border-color] :as popup-props}]
  (merge {:close-by-cover? true
          :fill-color :default}
         (if border-color {:border-width :xxs})
         (param popup-props)))
