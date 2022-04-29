
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.views
    (:require [plugins.item-viewer.body.prototypes :as body.prototypes]
              [plugins.item-viewer.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [reagent.api                         :as reagent]
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
  [_]
  ; TEMP
  [:div {:style {:width "100%"}}
        [:div {:style {:display "flex" :width "100%" :grid-row-gap "24px" :padding "12px 0 48px 0" :flex-direction "column" :align-items :center}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "60px" :width "60px"}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :width "200px"}}]]
        [:div {:style {:display "flex" :width "100%" :padding "12px 0" :justify-content :center}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :width "75%"}}]]
        [:div {:style {:display "flex" :width "100%" :padding "12px 0" :justify-content :center}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :width "75%"}}]]
        [:div {:style {:display "flex" :width "100%" :padding "12px 0" :justify-content "center"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "72px" :width "75%"}}]]])
  ; TEMP



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
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :item-element (metamorphic-content)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :label-key (keyword)(opt)
  ;    Only w/ {:auto-title? true}}
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
