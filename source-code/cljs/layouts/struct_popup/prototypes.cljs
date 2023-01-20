
(ns layouts.struct-popup.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @param (map) popup-props
  ; {}
  ;
  ; @return
  ; {}
  [{:keys [border-color close-by-cover?] :as popup-props}]
  (merge {:fill-color :default}
         (if border-color    {:border-position :all
                              :border-width    :xxs})
         (if close-by-cover? {:cover-color :black})
         (param popup-props)))
