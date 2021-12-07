
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.8.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.views
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [app-plugins.value-editor.engine :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-path (item-path vector)}
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :value-path (item-path vector)}
  [_ {:keys [edit-path] :as editor-props}]
  (merge (map/inherit editor-props [:label :validator])
         {:auto-focus? true
          :value-path  edit-path}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- disable-save-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [field-value (r elements/get-input-value db :value-editor/editor-field)
        validator   (r engine/get-editor-prop   db editor-id :validator)]
                    ; If validator is in use & field-value is NOT valid ...
       (boolean (or (and (some? validator)
                         (not ((:f validator) field-value)))
                    ; If field is required & field is empty ...
                    (and (r engine/get-editor-prop db editor-id :required?)
                         (r elements/field-empty?  db :value-editor/editor-field))))))

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:disable-save-button? (boolean)}
  [db [_ editor-id]]
  (merge (r engine/get-editor-props db editor-id)
         {:disable-save-button? (r disable-save-button? db editor-id)}))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r engine/get-editor-props db editor-id))

(a/reg-sub ::get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:disable-save-button? (boolean)
  ;   :save-button-label (metamorphic-content)}
  ;
  ; @return (component)
  [editor-id {:keys [disable-save-button? save-button-label]}]
  [elements/button ::save-button
                   {:disabled? disable-save-button?
                    :keypress  {:key-code 13 :required? true}
                    :on-click  [:value-editor/save-value! editor-id]
                    :label     save-button-label
                    :preset    :close-button
                    :indent    :right}])

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [editor-id _]
  [elements/button ::cancel-button
                   {:keypress {:key-code 27 :required? true}
                    :preset   :cancel-button
                    :on-click [:value-editor/cancel-editing! editor-id]
                    :indent   :left}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [editor-id header-props]
  [elements/polarity ::header
                     {:start-content [cancel-button editor-id header-props]
                      :end-content   [save-button   editor-id header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:helper (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [editor-id {:keys [helper]}]
  (if (some? helper)
      [:<> [elements/separator {:horizontal :horizontal :size :l}]
           [elements/text      {:content helper}]]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [editor-id body-props]
  (let [field-props (editor-props->field-props editor-id body-props)]
       [:<> [elements/separator  {:horizontal :horizontal :size :l}]
            [elements/text-field :value-editor/editor-field field-props]
            [editor-helper       editor-id body-props]
            [elements/separator  {:horizontal :horizontal :size :l}]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  (fn [_ [_ editor-id _]]
      [:ui/add-popup! editor-id
                      {:body   {:content #'body   :subscriber [::get-body-props   editor-id]}
                       :header {:content #'header :subscriber [::get-header-props editor-id]}}]))
