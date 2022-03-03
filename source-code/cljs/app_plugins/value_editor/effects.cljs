
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.effects
    (:require [app-plugins.value-editor.engine :as engine]
              [app-plugins.value-editor.events :as events]
              [app-plugins.value-editor.subs   :as subs]
              [app-plugins.value-editor.views  :as views]
              [mid-fruits.candy                :refer [param return]]
              [x.app-core.api                  :as a :refer [r]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :required? (boolean)
  ;   :save-button-label (metamorphic-content)(opt)
  ;   :value-path (item-path vector)}
  [extension-id editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:required?          true
          :save-button-label :save!
          :edit-path  (engine/default-edit-path extension-id editor-id)
          :value-path (engine/default-edit-path extension-id editor-id)}
         (param editor-props)
         (if edit-original? {:edit-path value-path})))



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
    (let [editor-props (editor-props-prototype extension-id editor-id editor-props)]
         {:db (r events/load-editor! db extension-id editor-id editor-props)
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
      [:ui/close-popup! (engine/view-id extension-id editor-id)]))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ extension-id editor-id]]
      {:db (r events/save-value! db extension-id editor-id)
       :dispatch-n [(r subs/get-on-save-event db extension-id editor-id)
                    [:ui/close-popup! (engine/view-id extension-id editor-id)]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [_ [_ extension-id editor-id]]
      [:ui/add-popup! (engine/view-id extension-id editor-id)
                      {:body   [views/body   extension-id editor-id];
                       :header [views/header extension-id editor-id]}]))
