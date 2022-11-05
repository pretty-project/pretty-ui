
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.views
    (:require [engines.item-handler.body.prototypes :as body.prototypes]
              [engines.item-handler.core.helpers    :as core.helpers]
              [reagent.api                          :as reagent]
              [re-frame.api                         :as r]
              [x.app-components.api                 :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  [handler-id]
  (if-let [ghost-element @(r/subscribe [:item-handler/get-body-prop handler-id :ghost-element])]
          [x.components/content ghost-element]))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  [handler-id]
  (if-let [error-element @(r/subscribe [:item-handler/get-body-prop handler-id :error-element])]
          [x.components/content error-element]))

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  [handler-id]
  (let [form-element @(r/subscribe [:item-handler/get-body-prop handler-id :form-element])]
       [x.components/content form-element]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  [handler-id]
  (cond @(r/subscribe [:item-handler/get-meta-item handler-id :engine-error])
         [error-element handler-id]
        @(r/subscribe [:item-handler/data-received? handler-id])
         [form-element handler-id]
         :data-not-received
         [ghost-element handler-id]))

(defn body
  ; @param (keyword) handler-id
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
  ;  [body :my-handler {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] [:div ...])
  ;  [body :my-handler {:form-element #'my-form-element}]
  [handler-id body-props]
  (let [body-props (body.prototypes/body-props-prototype handler-id body-props)]
       (reagent/lifecycles (core.helpers/component-id handler-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-handler/body-did-mount    handler-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-handler/body-will-unmount handler-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-handler/body-did-update   handler-id %]))
                            :reagent-render         (fn []              [body-structure                  handler-id])})))
