
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.body.views
    (:require [plugins.item-preview.body.prototypes :as body.prototypes]
              [plugins.item-preview.core.helpers    :as core.helpers]
              [reagent.api                          :as reagent]
              [re-frame.api                         :as r]
              [x.app-components.api                 :as components]))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id]
  (if-let [ghost-element @(r/subscribe [:item-preview/get-body-prop preview-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id]
  (let [preview-element @(r/subscribe [:item-preview/get-body-prop preview-id :preview-element])]
       [preview-element preview-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id]
  (cond @(r/subscribe [:item-preview/get-meta-item preview-id :error-mode?])
         ;[error-body preview-id {:error-description :the-item-you-opened-may-be-broken}]
         [:div "error"]
        @(r/subscribe [:item-preview/data-received? preview-id])
         [preview-element preview-id]
        :data-not-received
         [downloading-item preview-id]))

(defn body
  ; @param (keyword) preview-id
  ; @param (map) body-props
  ;  {:error-element (metamorphic-content)(opt)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :item-id (string)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :preview-element (metamorphic-content)
  ;   :query (vector)(opt)
  ;   :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ;  [item-preview/body :my-preview {...}]
  ;
  ; @usage
  ;  (defn my-preview-element [preview-id] [:div ...])
  ;  [item-preview/body :my-preview {:preview-element #'my-preview-element}]
  [preview-id body-props]
  (let [body-props (body.prototypes/body-props-prototype preview-id body-props)]
       (reagent/lifecycles (core.helpers/component-id preview-id :body)
                           {:reagent-render         (fn []              [body-structure                  preview-id])
                            :component-did-mount    (fn []  (r/dispatch [:item-preview/body-did-mount    preview-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-preview/body-will-unmount preview-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-preview/body-did-update   preview-id %]))})))
