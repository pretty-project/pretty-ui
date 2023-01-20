
(ns layouts.plain-popup.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [close-by-cover?] :as popup-props}]
  (merge {}
         (if close-by-cover? {:cover-color :black})
         (param popup-props)))
