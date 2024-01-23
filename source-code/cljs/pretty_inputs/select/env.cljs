
(ns pretty-inputs.select.env
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-engine.api :as pretty-engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-label
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (metamorphic-content)
  [select-id {:keys [option-label-f] :as select-props}]
  (if-let [picked-option (pretty-engine/get-picked-input-option select-id select-props)]
          (-> picked-option option-label-f metamorphic-content/compose)
          (-> :select!                     metamorphic-content/compose)))
