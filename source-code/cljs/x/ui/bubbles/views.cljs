
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.bubbles.views
    (:require [elements.api            :as elements]
              [re-frame.api            :as r]
              [reagent.api             :as reagent]
              [x.components.api        :as x.components]
              [x.ui.renderer.views     :rename {component renderer}]
              [x.ui.bubbles.config     :as bubbles.config]
              [x.ui.bubbles.helpers    :as bubbles.helpers]
              [x.ui.bubbles.prototypes :as bubbles.prototypes]))



;; -- Preset components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn state-changed-bubble-body
  ; @param (keyword) bubble-id
  ; @param (map) body-props
  ;  {:label (metamorphic-content)(opt)
  ;   :primary-button (map)(opt)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-event)}}
  ;
  ; @usage
  ;  [state-changed-bubble-body :my-bubble {...}]
  [bubble-id {:keys [label primary-button]}]
  [:<> (if label          [elements/label  {:content label :indent {:all :xs} :line-height :block}])
       (if primary-button [elements/button (bubbles.prototypes/primary-button-props-prototype primary-button)])])



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (if-let [user-close? @(r/subscribe [:x.ui/get-bubble-prop bubble-id :user-close?])]
          [elements/icon-button {:hover-color :highlight
                                 :on-click    [:x.ui/remove-bubble! bubble-id]
                                 :preset      :close}]))

(defn bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  ; - Az egyes bubble elemeken megjelenített egyszerű szöveges tartalom (keyword vagy string típus)
  ;   {:indent {:vertical :xs}} beállítással jelenik meg.
  ;
  ; - Az egyes bubble elemeken megjelenített ...
  ;   ... komponensek közvetlenül a bubble elem szélétől jelennek meg (esztétikai távolság nélkül).
  ;   ... szöveges tartalmak esztétikai távolság alkalmazásával jelennek meg.
  ;
  ; - Az egyes bubble elemeken megjelenített komponenseket azért szükséges közvetlenül a bubble elem
  ;   szélétől megjeleníteni, hogy a komponensben megjelenített (és a komponens jobb szélére igazított)
  ;   ikon-gombok a bubble elem saját bubble-close-icon-button komponensével megegyezően a bubble elem
  ;   széléhez igazítva jelenjenek meg.
  (let [body @(r/subscribe [:x.ui/get-bubble-prop bubble-id :body])]
       [:div.x-app-bubbles--element--body
         (cond (keyword? body) [elements/label bubble-id {:content body :indent {:vertical :xs} :line-height :block}]
               (string?  body) [elements/label bubble-id {:content body :indent {:vertical :xs} :line-height :block}]
               :default        [x.components/content bubble-id body])]))

(defn bubble-element-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  [:div (bubbles.helpers/bubble-attributes bubble-id)
        [bubble-body                       bubble-id]
        [bubble-close-icon-button          bubble-id]])

(defn bubble-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (let [on-mount   @(r/subscribe [:x.ui/get-bubble-prop bubble-id :on-mount])
        on-unmount @(r/subscribe [:x.ui/get-bubble-prop bubble-id :on-unmount])]
       (reagent/lifecycles bubble-id
                           {:reagent-render         (fn [] [bubble-element-structure bubble-id])
                            :component-did-mount    (fn [] (r/dispatch on-mount))
                            :component-will-unmount (fn [] (r/dispatch on-unmount))})))



;; -- Renderer components -----------------------------------------------------
;; ----------------------------------------------------------------------------

; TODO
; Az #x-app-bubbles elemre feltenni a {:data-scrollable-y true} beállítást!
(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered bubbles.config/MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
