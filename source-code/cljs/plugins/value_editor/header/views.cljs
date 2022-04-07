
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.header.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [disable-save-button? @(a/subscribe [:value-editor/disable-save-button? editor-id])
        save-button-label    @(a/subscribe [:value-editor/get-editor-prop      editor-id :save-button-label])]
       [elements/button ::save-button
                        {:disabled? disable-save-button? :label save-button-label
                         :keypress  {:key-code 13 :required? true} :preset :close-button
                         :on-click  [:value-editor/save-value! editor-id]}]))

(defn cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/button ::cancel-button
                   {:keypress {:key-code 27 :required? true} :preset :cancel-button
                    :on-click [:value-editor/cancel-editing! editor-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button editor-id]
                                 :end-content   [save-button   editor-id]}])
