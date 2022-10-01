
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
              [re-frame.api                        :as r]
              [x.app-components.api                :as components]))



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
  (if-let [ghost-element @(r/subscribe [:item-picker/get-body-prop picker-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  [picker-id]
  (let [preview-element @(r/subscribe [:item-picker/get-body-prop picker-id :preview-element])]
       [preview-element picker-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  [picker-id]
  (cond @(r/subscribe [:item-picker/get-meta-item picker-id :error-mode?])
         ;[error-body picker-id {:error-description :the-item-you-opened-may-be-broken}]
         [:div "error"]
        @(r/subscribe [:item-picker/data-received? picker-id])
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
  ;   :query (vector)(opt)
  ;   :transfer-id (keyword)(opt)
  ;    XXX#8173}
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
                            :component-did-mount    (fn []  (r/dispatch [:item-picker/body-did-mount    picker-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-picker/body-will-unmount picker-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-picker/body-did-update   picker-id %]))})))
