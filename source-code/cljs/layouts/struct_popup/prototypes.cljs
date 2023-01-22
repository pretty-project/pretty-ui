
(ns layouts.struct-popup.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (map) popup-props
  ; {}
  ;
  ; @return
  ; {}
  [{:keys [border-color on-cover] :as popup-props}]
  (merge {:fill-color :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (if on-cover     {:cover-color     :black})
         (param popup-props)))
