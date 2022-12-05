
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.body.views
    (:require [engines.item-preview.body.prototypes :as body.prototypes]
              [engines.item-preview.core.helpers    :as core.helpers]
              [re-frame.api                         :as r]
              [reagent.api                          :as reagent]
              [x.components.api                     :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id {:keys [ghost-element]}]
  (if-let [ghost-element ghost-element]
          [x.components/content preview-id ghost-element]))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id]
  (if-let [error-element @(r/subscribe [:item-preview/get-body-prop preview-id :error-element])]
          [x.components/content preview-id error-element]))

(defn preview-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id]
  (let [preview-element @(r/subscribe [:item-preview/get-body-prop preview-id :preview-element])]
       [x.components/content preview-id preview-element]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  [preview-id body-props]
  (cond @(r/subscribe [:item-preview/get-meta-item preview-id :engine-error])
         [error-element preview-id]
        @(r/subscribe [:item-preview/data-received? preview-id])
         [preview-element preview-id]
        @(r/subscribe [:item-preview/no-item-id-passed? preview-id])
         [:<>]
         :data-not-received
         [ghost-element preview-id body-props]))

(defn body
  ; @param (keyword) preview-id
  ; @param (map) body-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :error-element (metamorphic-content)(opt)
  ;  :ghost-element (metamorphic-content)(opt)
  ;  :item-id (string)
  ;  :item-path (vector)(opt)
  ;   Default: core.helpers/default-item-path
  ;  :preview-element (metamorphic-content)
  ;  :query (vector)(opt)
  ;  :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ; [body :my-preview {...}]
  ;
  ; @usage
  ; (defn my-preview-element [] [:div ...])
  ; [body :my-preview {:item-id "my-item"
  ;                    :preview-element #'my-preview-element}]
  [preview-id body-props]
  (let [body-props (body.prototypes/body-props-prototype preview-id body-props)]
       (reagent/lifecycles (core.helpers/component-id preview-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-preview/body-did-mount    preview-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-preview/body-will-unmount preview-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-preview/body-did-update   preview-id %]))
                            :reagent-render         (fn []              [body-structure                  preview-id body-props])})))
