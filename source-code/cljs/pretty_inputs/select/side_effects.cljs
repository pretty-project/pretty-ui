
(ns pretty-inputs.select.side-effects)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (string) field-content
  [select-id {:keys [add-option-f]} field-content]
  (if (-> field-content empty? not)
      (if add-option-f (add-option-f field-content))))
