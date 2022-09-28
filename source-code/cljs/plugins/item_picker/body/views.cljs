
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.body.views
    (:require [plugins.item-picker.body.prototypes :as body.prototypes]
              [plugins.item-picker.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [reagent.api                         :as reagent]
              [x.app-components.api                :as components]
              [x.app-core.api                      :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.views
(def error-body body.views/error-body)



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  [picker-id]
  (if-let [ghost-element @(a/subscribe [:item-picker/get-body-prop picker-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  [picker-id]
  (let [preview-element @(a/subscribe [:item-picker/get-body-prop picker-id :preview-element])]
       [preview-element picker-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  [editor-id]
  (cond @(a/subscribe [:item-picker/get-meta-item editor-id :error-mode?])
         ;[error-body editor-id {:error-description :the-item-you-opened-may-be-broken}]
         [:div "error"]
        @(a/subscribe [:item-picker/data-received? editor-id])
         [preview-element picker-id]
        :data-not-received
         [downloading-item picker-id]))

(defn body
  ; @param (keyword) picker-id
  ; @param (map) body-props
  ;  {:ghost-element (metamorphic-content)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :preview-element (metamorphic-content)
  ;   :query (vector)(opt)}
  ;
  ; @usage
  ;  [item-picker/body :my-picker {...}]
  ;
  ; @usage
  ;  (defn my-preview-element [picker-id] [:div ...])
  ;  [item-picker/body :my-picker {:preview-element #'my-preview-element}]
  [picker-id body-props]
  (let [body-props (body.prototypes/body-props-prototype picker-id body-props)]
       (reagent/lifecycles (core.helpers/component-id picker-id :body)
                           {:reagent-render         (fn []              [body-structure                 picker-id])
                            :component-did-mount    (fn []  (a/dispatch [:item-picker/body-did-mount    picker-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:item-picker/body-will-unmount picker-id]))
                            :component-did-update   (fn [%] (a/dispatch [:item-picker/body-did-update   picker-id %]))})))
