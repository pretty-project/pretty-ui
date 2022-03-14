
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.effects
    (:require [plugins.value-editor.core.events     :as core.events]
              [plugins.value-editor.core.helpers    :as core.helpers]
              [plugins.value-editor.core.prototypes :as core.prototypes]
              [plugins.value-editor.core.subs       :as core.subs]
              [plugins.value-editor.core.views      :as core.views]
              [x.app-core.api                       :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/load-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:value-path [...]}
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (string)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Default: false
  ;   :modifier (function)(opt)
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerkesztő mező aktuális értékét
  ;   :required? (boolean)(opt)
  ;    Default: true
  ;   :save-button-label (metamorphic-content)(opt)
  ;    Default: :save!
  ;   :validator (map)(opt)(constant)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)}
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [:value-editor/load-editor! :my-extension :my-editor {...}]
  (fn [{:keys [db]} [_ extension-id editor-id editor-props]]
    (let [editor-props (core.prototypes/editor-props-prototype extension-id editor-id editor-props)]
         {:db (r core.events/load-editor! db extension-id editor-id editor-props)
          :dispatch [:value-editor/render-editor! extension-id editor-id]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ extension-id editor-id]]
      [:ui/close-popup! (core.helpers/view-id extension-id editor-id)]))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ extension-id editor-id]]
      {:db (r core.events/save-value! db extension-id editor-id)
       :dispatch-n [(r core.subs/get-on-save-event db extension-id editor-id)
                    [:ui/close-popup! (core.helpers/view-id extension-id editor-id)]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [_ [_ extension-id editor-id]]
      [:ui/add-popup! (core.helpers/view-id extension-id editor-id)
                      {:body   [core.views/body   extension-id editor-id];
                       :header [core.views/header extension-id editor-id]}]))
