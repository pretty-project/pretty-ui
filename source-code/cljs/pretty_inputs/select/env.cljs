
(ns pretty-inputs.select.env
    (:require [metamorphic-content.api  :as metamorphic-content]
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
                   (metamorphic-content/compose option-label)
                   (metamorphic-content/compose :select!))))
