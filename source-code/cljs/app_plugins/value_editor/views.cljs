
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v1.0.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.views
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [app-plugins.value-editor.engine :as engine]
              [app-plugins.value-editor.subs   :as subs]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-path (item-path vector)}
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :value-path (item-path vector)}
  [_ _ {:keys [edit-path] :as editor-props}]
  (merge (select-keys editor-props [:label :validator])
         {:auto-focus? true
          :value-path  edit-path}))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:disable-save-button? (boolean)
  ;   :save-button-label (metamorphic-content)}
  ;
  ; @return (component)
  [extension-id editor-id {:keys [disable-save-button? save-button-label]}]
  [elements/button ::save-button
                   {:disabled? disable-save-button?
                    :keypress  {:key-code 13 :required? true}
                    :on-click  [:value-editor/save-value! extension-id editor-id]
                    :label     save-button-label
                    :preset    :close-button}])

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (component)
  [extension-id editor-id]
  [elements/button ::cancel-button
                   {:keypress {:key-code 27 :required? true}
                    :preset   :cancel-button
                    :on-click [:value-editor/cancel-editing! extension-id editor-id]}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id editor-id header-props]
  [elements/polarity ::header
                     {:start-content [cancel-button extension-id editor-id header-props]
                      :end-content   [save-button   extension-id editor-id header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:helper (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [extension-id editor-id {:keys [helper]}]
  (if (some? helper)
      [:<> [elements/horizontal-separator {:size :l}]
           [elements/text                 {:content helper}]]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id editor-id body-props]
  (let [field-props (editor-props->field-props extension-id editor-id body-props)]
       [:<> [elements/horizontal-separator {:size :l}]
            [elements/text-field :value-editor/editor-field field-props]
            [editor-helper extension-id editor-id body-props]
            [elements/horizontal-separator {:size :l}]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [_ [_ extension-id editor-id]]
      [:ui/add-popup! (engine/popup-id extension-id editor-id)
                      {:body   {:content #'body   :subscriber [:value-editor/get-body-props   extension-id editor-id]}
                       :header {:content #'header :subscriber [:value-editor/get-header-props extension-id editor-id]}}]))
