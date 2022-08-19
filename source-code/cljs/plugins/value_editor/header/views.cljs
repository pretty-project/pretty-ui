
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.header.views
    (:require [plugins.value-editor.core.helpers :as core.helpers]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]
              [x.app-ui.api                      :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [disable-save-button? @(a/subscribe [:value-editor/disable-save-button? editor-id])
        save-button-label    @(a/subscribe [:value-editor/get-editor-prop      editor-id :save-button-label])
        on-save               [:value-editor/save-value! editor-id]
        popup-id              (core.helpers/component-id editor-id :view)]
       [elements/horizontal-polarity ::header]))
;                                     {:start-content [ui/popup-cancel-button popup-id]
;                                      :end-content   [ui/popup-save-button   popup-id {:disabled? disable-save-button?
;                                                                                       :label     save-button-label
;                                                                                       :on-save   on-save]))
