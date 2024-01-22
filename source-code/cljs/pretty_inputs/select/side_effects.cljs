
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
  ; Pressing the ENTER key while the field is focused fires the 'on-enter-f' function
  ; that calls this ('add-option!') function
  (if (-> field-content empty? not)
      (if add-option-f (add-option-f field-content))))
