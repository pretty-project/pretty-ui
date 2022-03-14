
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.sticker-handler.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sticker-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sticker-props
  ;
  ; @return (map)
  ;  {:icon-family (keyword)}
  [sticker-props]
  (merge {:icon-family :material-icons-filled}
         (param sticker-props)))
