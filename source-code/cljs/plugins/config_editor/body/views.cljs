
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.body.views
    (:require [plugins.config-editor.body.prototypes :as body.prototypes]
              [plugins.config-editor.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views     :as body.views]
              [reagent.api                           :as reagent]
              [x.app-components.api                  :as components]
              [x.app-core.api                        :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.views
(def error-body body.views/error-body)



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [ghost-element @(a/subscribe [:config-editor/get-body-prop editor-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [form-element @(a/subscribe [:config-editor/get-body-prop editor-id :form-element])]
       [form-element editor-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (cond @(a/subscribe [:config-editor/get-meta-item editor-id :error-mode?])
         [error-body editor-id {:error-description :the-content-you-opened-may-be-broken}]
        @(a/subscribe [:config-editor/data-received? editor-id])
         [form-element editor-id]
        :data-not-received
         [downloading-config editor-id]))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:config-path (vector)(opt)
  ;    Default: core.helpers/default-config-path
  ;   :form-element (metamorphic-content)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :query (vector)(opt)}
  ;
  ; @usage
  ;  [config-editor/body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [editor-id] [:div ...])
  ;  [config-editor/body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [body-props (body.prototypes/body-props-prototype editor-id body-props)]
       (reagent/lifecycles (core.helpers/component-id editor-id :body)
                           {:reagent-render         (fn []              [body-structure                 editor-id])
                            :component-did-mount    (fn []  (a/dispatch [:config-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:config-editor/body-will-unmount editor-id]))
                            :component-did-update   (fn [%] (a/dispatch [:config-editor/body-did-update   editor-id %]))})))
