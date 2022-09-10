
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.views
    (:require [plugins.item-viewer.body.prototypes :as body.prototypes]
              [plugins.item-viewer.core.helpers    :as core.helpers]
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
  ; @param (keyword) viewer-id
  [viewer-id]
  (if-let [ghost-element @(a/subscribe [:item-viewer/get-body-prop viewer-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [item-element @(a/subscribe [:item-viewer/get-body-prop viewer-id :item-element])]
       [item-element viewer-id]))

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (cond @(a/subscribe [:item-viewer/get-meta-item viewer-id :error-mode?])
         [error-body viewer-id {:error-description :the-item-you-opened-may-be-broken}]
        @(a/subscribe [:item-viewer/data-received? viewer-id])
         [item-element viewer-id]
        :data-not-received
         [downloading-item viewer-id]))

(defn body
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:label-key ...}
  ;   :default-item-id (string)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :item-element (metamorphic-content)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :label-key (keyword)(opt)
  ;    Only w/ {:auto-title? true}
  ;   :query (vector)(opt)}
  ;
  ; @usage
  ;  [item-viewer/body :my-viewer {...}]
  ;
  ; @usage
  ;  (defn my-item-element [viewer-id] [:div ...])
  ;  [item-viewer/body :my-viewer {:item-element #'my-item-element}]
  [viewer-id body-props]
  (let [body-props (body.prototypes/body-props-prototype viewer-id body-props)]
       (reagent/lifecycles (core.helpers/component-id viewer-id :body)
                           {:reagent-render         (fn []              [body-structure                 viewer-id])
                            :component-did-mount    (fn []  (a/dispatch [:item-viewer/body-did-mount    viewer-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:item-viewer/body-will-unmount viewer-id]))
                            :component-did-update   (fn [%] (a/dispatch [:item-viewer/body-did-update   viewer-id %]))})))
