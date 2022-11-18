
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.body.views
    (:require [engines.item-editor.body.prototypes :as body.prototypes]
              [engines.item-editor.core.helpers    :as core.helpers]
              [reagent.api                         :as reagent]
              [re-frame.api                        :as r]
              [x.components.api                    :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [ghost-element @(r/subscribe [:item-editor/get-body-prop editor-id :ghost-element])]
          [x.components/content editor-id ghost-element]))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [error-element @(r/subscribe [:item-editor/get-body-prop editor-id :error-element])]
          [x.components/content editor-id error-element]))

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [form-element @(r/subscribe [:item-editor/get-body-prop editor-id :form-element])]
       [x.components/content editor-id form-element]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (cond @(r/subscribe [:item-editor/get-meta-item editor-id :engine-error])
         [error-element editor-id]
        @(r/subscribe [:item-editor/data-received? editor-id])
         [form-element editor-id]
         :data-not-received
         [ghost-element editor-id]))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;    W/ {:label-key ...}
  ;   :default-item (map)(opt)
  ;   :form-element (metamorphic-content)
  ;   :error-element (metamorphic-content)(opt)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :initial-item (map)(opt)
  ;   :item-id (string)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :on-saved (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szervertől visszaérkező elemet.
  ;   :label-key (keyword)(opt)
  ;    W/ {:auto-title? true}
  ;   :query (vector)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)
  ;   :suggestions-path (vector)(opt)
  ;    Default: core.helpers/default-suggestions-path
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
                           {:component-did-mount    (fn []  (r/dispatch [:item-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-editor/body-will-unmount editor-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-editor/body-did-update   editor-id %]))
                            :reagent-render         (fn []              [body-structure                 editor-id])})))
