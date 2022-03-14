
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.views
    (:require [plugins.value-editor.core.helpers :as core.helpers]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-elements.api                :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  (let [disable-save-button? @(a/subscribe [:value-editor/disable-save-button? extension-id editor-id])
        save-button-label    @(a/subscribe [:value-editor/get-meta-item        extension-id editor-id :save-button-label])]
       [elements/button ::save-button
                        {:disabled? disable-save-button? :label save-button-label
                         :keypress  {:key-code 13 :required? true} :preset :close-button
                         :on-click  [:value-editor/save-value! extension-id editor-id]}]))

(defn cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  [elements/button ::cancel-button
                   {:keypress {:key-code 27 :required? true} :preset :cancel-button
                    :on-click [:value-editor/cancel-editing! extension-id editor-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button extension-id editor-id]
                                 :end-content   [save-button   extension-id editor-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  (if-let [helper @(a/subscribe [:value-editor/get-meta-item extension-id editor-id :helper])]
          [:<> [elements/horizontal-separator {:size :l}]
               [elements/text                 {:content helper}]]))

(defn editor-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  (let [field-props (core.helpers/editor-props->field-props extension-id editor-id)]
       [elements/text-field :value-editor/editor-field field-props]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  [extension-id editor-id]
  [:<> [elements/horizontal-separator {:size :l}]
       [editor-field  extension-id editor-id]
       [editor-helper extension-id editor-id]
       [elements/horizontal-separator {:size :l}]])