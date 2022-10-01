
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
              [reagent.api                         :as reagent]
              [re-frame.api                        :as r]
              [x.app-components.api                :as components]))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (if-let [ghost-element @(r/subscribe [:item-viewer/get-body-prop viewer-id :ghost-element])]
          [components/content ghost-element]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [error-element @(r/subscribe [:item-lister/get-body-prop viewer-id :error-element])]
       [error-element viewer-id]))

(defn- item-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (let [item-element @(r/subscribe [:item-viewer/get-body-prop viewer-id :item-element])]
       [item-element viewer-id]))

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  [viewer-id]
  (cond @(r/subscribe [:item-viewer/get-meta-item viewer-id :error-mode?])
         [error-element viewer-id]
        @(r/subscribe [:item-viewer/data-received? viewer-id])
         [item-element viewer-id]
        :data-not-received
         [downloading-item viewer-id]))

(defn body
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:label-key ...}
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :item-element (metamorphic-content)
  ;   :item-id (string)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :label-key (keyword)(opt)
  ;    Only w/ {:auto-title? true}
  ;   :query (vector)(opt)
  ;   :transfer-id (keyword)(opt)}
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
                            :component-did-mount    (fn []  (r/dispatch [:item-viewer/body-did-mount    viewer-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-viewer/body-will-unmount viewer-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-viewer/body-did-update   viewer-id %]))})))
