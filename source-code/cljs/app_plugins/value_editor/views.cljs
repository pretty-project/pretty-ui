
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v1.1.0
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.views
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [app-plugins.value-editor.engine :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) editor-props
  ;  {:edit-path (item-path vector)}
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :min-width (keyword)
  ;   :value-path (item-path vector)}
  [{:keys [edit-path] :as editor-props}]
  (merge (select-keys editor-props [:label :modifier :validator])
         {:auto-focus? true
          :min-width   :l
          :value-path  edit-path}))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-button
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

(defn cancel-button
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

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id editor-id header-props]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button extension-id editor-id header-props]
                                 :end-content   [save-button   extension-id editor-id header-props]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (component)
  [extension-id editor-id]
  (let [header-props (a/subscribe [:value-editor/get-header-props extension-id editor-id])]
       (fn [] [header-structure extension-id editor-id @header-props])))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:helper (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ _ {:keys [helper]}]
  (if helper [:<> [elements/horizontal-separator {:size :l}]
                  [elements/text                 {:content helper}]]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id editor-id body-props]
  (let [field-props (editor-props->field-props body-props)]
       [:<> [elements/horizontal-separator {:size :l}]
            [elements/text-field :value-editor/editor-field field-props]
            [editor-helper extension-id editor-id body-props]
            [elements/horizontal-separator {:size :l}]]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (component)
  [extension-id editor-id]
  (let [body-props (a/subscribe [:value-editor/get-body-props extension-id editor-id])]
       (fn [] [body-structure extension-id editor-id @body-props])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [_ [_ extension-id editor-id]]
      [:ui/add-popup! (engine/view-id extension-id editor-id)
                      {:body   [body   extension-id editor-id];
                       :header [header extension-id editor-id]}]))
