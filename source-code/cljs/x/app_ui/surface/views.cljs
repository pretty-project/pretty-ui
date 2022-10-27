
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.views
    (:require [re-frame.api             :as r]
              [reagent.api              :as reagent]
              [x.app-components.api     :as components]
              [x.app-environment.api    :as environment]
              [x.app-ui.renderer        :rename {component renderer}]
              [x.app-ui.surface.helpers :as surface.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  (let [content @(r/subscribe [:ui/get-surface-prop surface-id :content])]
       [:div.x-app-surface--element--content [components/content surface-id content]]))

(defn surface-element-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  [:div (surface.helpers/surface-attributes surface-id)
        [surface-content                    surface-id]])

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  ; environment/reset-scroll-y!
  ; A felület kirenderelése után azonnal, szükséges a scroll-y értékét alaphelyzetbe
  ; állítani, hogy a kirenderelt felület tartalma ne az előző felülettől "örökölt"
  ; scroll-y értéken jelenjen meg!
  ;
  ; Előfordulhat, hogy egy felületen belül az egymás alatti szekciók,
  ; különböző útvonalakhoz tartoznak (pl. weboldalaknál), ilyenkor az egyes útvonalak
  ; újra meghívják a [:ui/render-surface! ...] eseményt, ami NEM rendereli ki újra
  ; ugyanazt a felületet, tehát a component-did-mount életciklus nem történik meg,
  ; ezért nem állítódik alaphelyzetbe a scroll-y értéke (ez jó)!
  (let [on-mount   @(r/subscribe [:ui/get-surface-prop surface-id :on-mount])
        on-unmount @(r/subscribe [:ui/get-surface-prop surface-id :on-unmount])]
       (reagent/lifecycles surface-id
                           {:reagent-render         (fn [] [surface-element-structure surface-id])
                            :component-did-mount    (fn [] (r/dispatch on-mount)
                                                           (environment/reset-scroll-y!))
                            :component-will-unmount (fn [] (r/dispatch on-unmount))})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :surface {:element               #'surface-element
                      :max-elements-rendered 1
                      :queue-behavior        :push
                      :required?             true
                      :rerender-same?        false}])
