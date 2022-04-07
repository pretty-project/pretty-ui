
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.body.views
    (:require [plugins.value-editor.body.helpers :as body.helpers]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [helper @(a/subscribe [:value-editor/get-editor-prop editor-id :helper])]
          [:<> [elements/horizontal-separator {:size :l}]
               [elements/text                 {:content helper}]]))

(defn editor-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [field-props (body.helpers/editor-props->field-props editor-id)]
       [elements/text-field :value-editor/editor-field field-props]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [:<> [elements/horizontal-separator {:size :l}]
       [editor-field  editor-id]
       [editor-helper editor-id]
       [elements/horizontal-separator {:size :l}]])
