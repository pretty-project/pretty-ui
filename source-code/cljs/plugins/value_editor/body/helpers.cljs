
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.body.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :min-width (keyword)
  ;   :value-path (vector)}
  [editor-id]
  (let [editor-props @(a/subscribe [:value-editor/get-editor-props editor-id])]
       (merge (select-keys editor-props [:initial-value :label :modifier :validator])
              {:auto-focus? true
               :min-width   :l
               :value-path  (:edit-path editor-props)})))
