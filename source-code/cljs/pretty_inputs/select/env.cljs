
(ns pretty-inputs.select.env
    (:require [multitype-content.api  :as multitype-content]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-label
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (multitype-content)
  [select-id select-props]
  (if-let [picked-option (pretty-inputs.engine/get-picked-input-option select-id select-props)]
          (if-some [option-label (pretty-inputs.engine/get-input-option-label select-id select-props picked-option)]
                   (multitype-content/compose option-label)
                   (multitype-content/compose :select!))))
