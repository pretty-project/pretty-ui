
(ns pretty-inputs.date-field.views
    (:require [fruits.random.api :as random]
              [pretty-inputs.date-field.prototypes :as date-field.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword)(opt) id
  ; @param (map) props
  ;
  ; @usage (pretty-inputs/date-field.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props])))
