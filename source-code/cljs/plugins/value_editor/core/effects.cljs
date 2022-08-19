

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.effects
    (:require [plugins.value-editor.body.views      :as body.views]
              [plugins.value-editor.core.events     :as core.events]
              [plugins.value-editor.core.helpers    :as core.helpers]
              [plugins.value-editor.core.prototypes :as core.prototypes]
              [plugins.value-editor.core.subs       :as core.subs]
              [plugins.value-editor.header.views    :as header.views]
              [x.app-core.api                       :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/load-editor!
  ; @param (keyword)(opt) editor-id
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
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [:value-editor/load-editor! {...}]
  ;
  ; @usage
  ;  [:value-editor/load-editor! :my-editor {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ editor-id editor-props]]
      (let [editor-props (core.prototypes/editor-props-prototype editor-id editor-props)]
           {:db       (r core.events/load-editor! db editor-id editor-props)
            :dispatch [:value-editor/render-editor! editor-id]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      [:ui/close-popup! (core.helpers/component-id editor-id :view)]))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db         (r core.events/save-value! db editor-id)
       :dispatch-n [(r core.subs/get-on-save-event db editor-id)
                    [:ui/close-popup! (core.helpers/component-id editor-id :view)]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/render-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      [:ui/render-popup! (core.helpers/component-id editor-id :view)
                         {:body   [body.views/body     editor-id]
                          :header [header.views/header editor-id]}]))
