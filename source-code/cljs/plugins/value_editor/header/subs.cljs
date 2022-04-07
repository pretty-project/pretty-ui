
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.header.subs
    (:require [plugins.value-editor.core.subs :as core.subs]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-elements.api             :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-save-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (let [field-value (r elements/get-input-value  db :value-editor/editor-field)
        validator   (r core.subs/get-editor-prop db editor-id :validator)]
       (boolean (or ; If validator is in use & field-value is NOT valid ...
                    (and validator (not ((:f validator) field-value)))
                    ; If field is required & field is empty ...
                    (and (r core.subs/get-editor-prop db editor-id :required?)
                         (r elements/field-empty?     db :value-editor/editor-field))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :value-editor/disable-save-button? disable-save-button?)
