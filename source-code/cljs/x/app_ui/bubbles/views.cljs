
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.views
    (:require [re-frame.api                :as r]
              [reagent.api                 :as reagent]
              [x.app-components.api        :as components]
              [x.app-elements.api          :as elements]
              [x.app-ui.renderer           :rename {component renderer}]
              [x.app-ui.bubbles.config     :as bubbles.config]
              [x.app-ui.bubbles.helpers    :as bubbles.helpers]
              [x.app-ui.bubbles.prototypes :as bubbles.prototypes]))



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
  [:<> (if label          [elements/label  {:content label :indent {:all :xs}}])
       (if primary-button [elements/button (bubbles.prototypes/primary-button-props-prototype primary-button)])])



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (if-let [user-close? @(r/subscribe [:ui/get-bubble-prop bubble-id :user-close?])]
          [elements/icon-button {:on-click [:ui/remove-bubble! bubble-id]
                                 :preset   :close}]))

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
  (let [body @(r/subscribe [:ui/get-bubble-prop bubble-id :body])]
       [:div.x-app-bubbles--element--body
         (cond (keyword? body) [elements/label bubble-id {:content body :indent {:vertical :xs}}]
               (string?  body) [elements/label bubble-id {:content body :indent {:vertical :xs}}]
               :default        [components/content bubble-id body])]))

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
  (let [on-bubble-closed   @(r/subscribe [:ui/get-bubble-prop bubble-id :on-bubble-closed])
        on-bubble-rendered @(r/subscribe [:ui/get-bubble-prop bubble-id :on-bubble-rendered])]
       (reagent/lifecycles bubble-id
                           {:reagent-render         (fn [] [bubble-element-structure bubble-id])
                            :component-will-unmount (fn [] (r/dispatch on-bubble-closed))
                            :component-did-mount    (fn [] (r/dispatch on-bubble-rendered))})))



;; -- Renderer components -----------------------------------------------------
;; ----------------------------------------------------------------------------

; TODO
; Az #x-app-bubbles elemre feltenni a {:data-hide-scrollbar true} beállítást!
(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered bubbles.config/MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
