
(ns pretty-inputs.select.env
    (:require [fruits.string.api       :as string]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-inputs.core.env  :as core.env]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-label
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (metamorphic-content)
  [select-id select-props]
  (if-let [selected-option-label @(r/subscribe [:pretty-inputs.select/get-selected-option-label select-id select-props])]
          (-> selected-option-label metamorphic-content/compose)
          (-> :select!              metamorphic-content/compose)))
