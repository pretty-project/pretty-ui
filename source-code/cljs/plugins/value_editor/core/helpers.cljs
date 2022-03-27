
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.helpers
    (:require [mid.plugins.value-editor.core.helpers :as core.helpers]
              [x.app-core.api :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.value-editor.core-helpers
(def component-id      core.helpers/component-id)
(def default-edit-path core.helpers/default-edit-path)



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
