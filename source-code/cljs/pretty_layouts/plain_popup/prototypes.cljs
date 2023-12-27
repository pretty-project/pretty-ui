
(ns pretty-layouts.plain-popup.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
