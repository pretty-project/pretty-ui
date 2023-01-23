
(ns layouts.plain-popup.prototypes
    (:require [noop.api :refer [param]]))

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
         (param popup-props)))
