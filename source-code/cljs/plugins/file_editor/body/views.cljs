
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.body.views
    (:require [plugins.file-editor.body.prototypes :as body.prototypes]
              [plugins.file-editor.core.helpers    :as core.helpers]
              [reagent.api                         :as reagent]
              [x.app-components.api                :as x.components]
              [re-frame.api                        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [ghost-element @(r/subscribe [:file-editor/get-body-prop editor-id :ghost-element])]
          [x.components/content ghost-element]))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [error-element @(r/subscribe [:file-editor/get-body-prop editor-id :error-element])]
          [x.components/content error-element]))

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [form-element @(r/subscribe [:file-editor/get-body-prop editor-id :form-element])]
       [x.components/content form-element]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (cond @(r/subscribe [:file-editor/get-meta-item editor-id :error-mode?])
         [error-element editor-id]
        @(r/subscribe [:file-editor/data-received? editor-id])
         [form-element editor-id]
         :data-not-received
         [ghost-element editor-id]))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:content-path (vector)(opt)
  ;    Default: core.helpers/default-content-path
  ;   :default-content (map)(opt)
  ;   :error-element (metamorphic-content)(opt)
  ;   :form-element (metamorphic-content)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :on-saved (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szervertől visszaérkező tartalmat.
  ;   :query (vector)(opt)
  ;   :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ;  [body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] [:div ...])
  ;  [body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [body-props (body.prototypes/body-props-prototype editor-id body-props)]
       (reagent/lifecycles (core.helpers/component-id editor-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:file-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:file-editor/body-will-unmount editor-id]))
                            :component-did-update   (fn [%] (r/dispatch [:file-editor/body-did-update   editor-id %]))
                            :reagent-render         (fn []              [body-structure                 editor-id])})))
