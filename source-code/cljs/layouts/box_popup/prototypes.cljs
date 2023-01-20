
(ns layouts.box-popup.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color close-by-cover?] :as popup-props}]
  (merge {}
         (if border-color    {:border-position :all
                              :border-width    :xxs})
         (if close-by-cover? {:cover-color :black})
         (param popup-props)))
