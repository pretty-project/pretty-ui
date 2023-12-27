
(ns pretty-layouts.struct-popup.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
         (-> popup-props)))
